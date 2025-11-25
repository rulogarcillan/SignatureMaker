package com.signaturemaker.app.features.sign

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import androidx.activity.compose.LocalActivity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.github.gcacace.signaturepad.views.SignaturePad
import com.signaturemaker.app.R
import com.signaturemaker.app.ui.designsystem.SMTheme
import com.signaturemaker.app.ui.designsystem.components.SMButton
import com.signaturemaker.app.ui.designsystem.components.SMColorSelector
import com.signaturemaker.app.ui.designsystem.components.SMIconButton
import com.signaturemaker.app.ui.designsystem.components.SMImageSelector
import com.signaturemaker.app.ui.designsystem.components.SMLineSeparator
import com.signaturemaker.app.ui.designsystem.components.SMModalBottomSheet
import com.signaturemaker.app.ui.designsystem.components.SMText
import com.signaturemaker.app.utils.shareSign
import org.koin.androidx.compose.koinViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignScreen(
    modifier: Modifier = Modifier,
    viewModel: SignBoardViewModel = koinViewModel()
) {
    val signState: SignState = rememberSignState(
        initialColor = SMTheme.color.pen1,
        initialImage = R.drawable.mascara3
    )

    // Handler function with state hoisting pattern
    val onAction: (SignAction) -> Unit = { action ->
        handleSignAction(action, viewModel)
    }

    val saveBitmapEvent by viewModel.saveBitmap.observeAsState()
    val failureEvent by viewModel.failure.observeAsState()

    saveBitmapEvent?.getContentIfNotHandled()?.let { uriResponse ->

        if (uriResponse.share) {
            LocalActivity.current?.shareSign(uriResponse.uri)
        } else {
            println("Mostrar mensaje de guardado para URI: ${uriResponse.uri}")
        }
    }

    failureEvent?.getContentIfNotHandled()?.let { failure ->
        println("Error al guardar la firma: $failure")
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(tiledBackgroundBrush(signState.selectedImage))
    ) {
        MyXmlTextView(signState)
        OptionModalBottomSheet(
            signState = signState,
            onAction = onAction
        )
        if (signState.showSignHere) {
            SMText(text = stringResource(R.string.title_SingHere), modifier = Modifier.align(Alignment.Center))
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SMTheme.spacing.spacing150),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SMButton(
                text = stringResource(R.string.title_options),
                onClick = { signState.showBottomSheet() }
            )
        }
    }
}

@Composable
fun MyXmlTextView(signState: SignState) {
    AndroidView(factory = { context ->
        LayoutInflater.from(context).inflate(R.layout.signature_pad, null, false)
    }, update = { view ->
        view as SignaturePad

        // Store reference to SignaturePad
        signState.signaturePadRef = view

        view.setPenColor(signState.selectedColor.toArgb())
        view.setMinWidth(signState.strokeWidthRange.start)
        view.setMaxWidth(signState.strokeWidthRange.endInclusive)
        view.setVelocityFilterWeight(0.1f)


        signState.clearFunction = {
            view.clear()
        }
        view.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                signState.updateShowSignHere(false)
            }

            override fun onClear() {
                signState.updateShowSignHere(true)
            }

            override fun onSigned() {
                signState.updateShowSignHere(false)
            }
        })
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionModalBottomSheet(
    signState: SignState,
    onAction: (SignAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorPen = listOf(SMTheme.color.pen1, SMTheme.color.pen2, SMTheme.color.pen3, SMTheme.color.pen4)
    val imageBackground = listOf(
        R.drawable.mascara3,
        R.drawable.mascara1,
        R.drawable.mascara2,
    )

    SMModalBottomSheet(
        onDismissRequest = { signState.closeBottomSheet() },
        sheetState = signState.sheetState,
        modifier = modifier,
        openBottomSheet = signState.isShowBottomSheet,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SMTheme.spacing.spacing200)
        ) {
            // Header con título
            SMText(
                text = stringResource(R.string.title_signature_options),
                style = SMTheme.material.typography.headlineSmall,
                color = SMTheme.material.colorScheme.onSurface,
                modifier = Modifier.padding(
                    horizontal = SMTheme.spacing.spacing200,
                    vertical = SMTheme.spacing.spacing150
                )
            )

            // Opciones principales
            SectionOption(title = stringResource(R.string.title_actions)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SMTheme.spacing.spacing100),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SMIconButton(
                        imageVector = Icons.Default.CleaningServices,
                        label = stringResource(R.string.tittle_clean),
                        color = SMTheme.material.colorScheme.onSurfaceVariant,
                        onClick = {
                            signState.clearSignature()
                            signState.closeBottomSheet()
                        },
                    )

                    Box {
                        SMIconButton(
                            imageVector = Icons.Default.Save,
                            label = stringResource(R.string.title_bSave),
                            color = SMTheme.material.colorScheme.onSurfaceVariant,
                            onClick = { signState.showSaveDropdown() },
                        )

                        DropdownMenu(
                            expanded = signState.showSaveDropdown,
                            onDismissRequest = { signState.closeSaveDropdown() }
                        ) {
                            DropdownMenuItem(
                                text = { SMText(text = stringResource(R.string.title_save_png)) },
                                onClick = {
                                    onAction(
                                        SignAction.SavePngTransparent(
                                            bitmap = signState.getTransparentSignatureBitmap()
                                        )
                                    )
                                    signState.closeSaveDropdown()
                                    signState.closeBottomSheet()
                                }
                            )
                            DropdownMenuItem(
                                text = { SMText(text = stringResource(R.string.title_save_png_wh)) },
                                onClick = {
                                    onAction(
                                        SignAction.SavePngWhiteBackground(
                                            bitmap = signState.getWhiteBackgroundSignatureBitmap()
                                        )
                                    )
                                    signState.closeSaveDropdown()
                                    signState.closeBottomSheet()
                                }
                            )
                        }
                    }

                    Box {
                        SMIconButton(
                            imageVector = Icons.Default.Share,
                            label = stringResource(R.string.share),
                            color = SMTheme.material.colorScheme.onSurfaceVariant,
                            onClick = { signState.showShareDropdown() },
                        )

                        DropdownMenu(
                            expanded = signState.showShareDropdown,
                            onDismissRequest = { signState.closeShareDropdown() }
                        ) {
                            DropdownMenuItem(
                                text = { SMText(text = stringResource(R.string.title_save_png)) },
                                onClick = {
                                    onAction(
                                        SignAction.SharePngTransparent(
                                            bitmap = signState.getTransparentSignatureBitmap()
                                        )
                                    )
                                    signState.closeShareDropdown()
                                    signState.closeBottomSheet()
                                }
                            )
                            DropdownMenuItem(
                                text = { SMText(text = stringResource(R.string.title_save_png_wh)) },
                                onClick = {
                                    onAction(
                                        SignAction.SharePngWhiteBackground(
                                            bitmap = signState.getWhiteBackgroundSignatureBitmap()
                                        )
                                    )
                                    signState.closeShareDropdown()
                                    signState.closeBottomSheet()
                                }
                            )
                        }
                    }

                }
            }

            // Separador visual
            Spacer(modifier = Modifier.height(SMTheme.spacing.spacing50))

            // Color del trazo
            SectionOption(title = stringResource(R.string.title_bColor)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SMTheme.spacing.spacing100),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    colorPen.forEach { color ->
                        SMColorSelector(
                            color = color,
                            selected = color == signState.selectedColor,
                            modifier = Modifier.size(SMTheme.size.size500),
                            onClick = { signState.updateColor(color) }
                        )
                    }
                }
            }

            // Grosor del trazo
            SectionOption(title = stringResource(R.string.title_bStroke)) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RangeSlider(
                        value = signState.strokeWidthRange,
                        onValueChange = { newValues ->
                            signState.updateStrokeWidth(newValues)
                        },
                        onValueChangeFinished = {},
                        valueRange = ValueRangeSlider,
                        steps = 0,
                        modifier = Modifier.padding(horizontal = SMTheme.spacing.spacing100)
                    )
                    Spacer(modifier = Modifier.height(SMTheme.spacing.spacing50))
                    SMText(
                        text = "${stringResource(R.string.min)} ${"%.1f".format(signState.strokeWidthRange.start)} • ${
                            stringResource(
                                R.string.max
                            )
                        } ${"%.1f".format(signState.strokeWidthRange.endInclusive)}",
                        style = SMTheme.material.typography.bodyMedium,
                        color = SMTheme.material.colorScheme.onSurfaceVariant
                    )
                }
            }


            // Fondo
            SectionOption(title = stringResource(R.string.title_background)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SMTheme.spacing.spacing100),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    imageBackground.forEach { image ->
                        SMImageSelector(
                            image = painterResource(id = image),
                            selected = image == signState.selectedImage,
                            modifier = Modifier.size(SMTheme.size.size500),
                            onClick = { signState.updateImage(image) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionOption(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = SMTheme.spacing.spacing200,
                vertical = SMTheme.spacing.spacing100
            )
    ) {
        SMText(
            text = title,
            style = SMTheme.material.typography.titleMedium,
            color = SMTheme.material.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(SMTheme.spacing.spacing150))
        content()
        Spacer(modifier = Modifier.height(SMTheme.spacing.spacing100))
        // Separador sutil
        SMLineSeparator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = SMTheme.spacing.spacing150)
        )
    }
}

@Composable
fun tiledBackgroundBrush(@DrawableRes imageRes: Int): Brush {
    val imageBitmap = ImageBitmap.imageResource(LocalResources.current, imageRes)
    val shader = ImageShader(imageBitmap, TileMode.Repeated, TileMode.Repeated)
    return ShaderBrush(shader)
}


private val ValueRangeSlider = 1f..10f
