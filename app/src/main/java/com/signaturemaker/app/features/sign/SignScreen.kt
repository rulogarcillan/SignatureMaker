package com.signaturemaker.app.features.sign

import android.view.LayoutInflater
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignScreen(
    modifier: Modifier = Modifier,
) {
    val signState: SignState = rememberSignState(
        initialColor = SMTheme.color.pen1,
        initialImage = R.drawable.mascara3
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(tiledBackgroundBrush(signState.selectedImage))
    ) {
        MyXmlTextView(signState)
        OptionModalBottomSheet(signState = signState)
        if (signState.showSignHere) {
            SMText(text = "Sign here", modifier = Modifier.align(Alignment.Center))
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SMTheme.spacing.spacing150),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SMButton(
                text = "Options",
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
                text = "Signature Options",
                style = SMTheme.material.typography.headlineSmall,
                color = SMTheme.material.colorScheme.onSurface,
                modifier = Modifier.padding(
                    horizontal = SMTheme.spacing.spacing200,
                    vertical = SMTheme.spacing.spacing150
                )
            )

            // Opciones principales
            SectionOption(title = "Actions") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SMTheme.spacing.spacing100),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SMIconButton(
                        imageVector = Icons.Default.CleaningServices,
                        label = "Clear",
                        color = SMTheme.material.colorScheme.onSurfaceVariant,
                        onClick = {
                            signState.clearSignature()
                            signState.closeBottomSheet()
                        },
                    )

                    SMIconButton(
                        imageVector = Icons.Default.Save,
                        label = "Save",
                        color = SMTheme.material.colorScheme.onSurfaceVariant,
                        onClick = { signState.closeBottomSheet() },
                    )

                    SMIconButton(
                        imageVector = Icons.Default.Share,
                        label = "Share",
                        color = SMTheme.material.colorScheme.onSurfaceVariant,
                        onClick = { signState.closeBottomSheet() },
                    )
                }
            }

            // Separador visual
            Spacer(modifier = Modifier.height(SMTheme.spacing.spacing50))

            // Color del trazo
            SectionOption(title = "Stroke color") {
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
            SectionOption(title = "Stroke width") {
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
                        valueRange = 1f..10f,
                        steps = 0,
                        modifier = Modifier.padding(horizontal = SMTheme.spacing.spacing100)
                    )
                    Spacer(modifier = Modifier.height(SMTheme.spacing.spacing50))
                    SMText(
                        text = "Min: ${"%.1f".format(signState.strokeWidthRange.start)} • Max: ${"%.1f".format(signState.strokeWidthRange.endInclusive)}",
                        style = SMTheme.material.typography.bodyMedium,
                        color = SMTheme.material.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Fondo
            SectionOption(title = "Background") {
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
