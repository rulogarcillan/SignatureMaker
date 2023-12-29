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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
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
        selectedColor = SMTheme.color.pen1,
        selectedImage = R.drawable.mascara3
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(tiledBackgroundBrush(signState.backgroundState.selectedImage))
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
                onClick = { signState.bottomSheetState.showBottomSheet() }
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
        view.setPenColor(signState.strokeColorState.selectedColor.toArgb())
        signState.clearFunction = {
            view.clear()
        }
        view.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                signState.showSignHereChange(false)
            }

            override fun onClear() {
                signState.showSignHereChange(true)
            }

            override fun onSigned() {
                signState.showSignHereChange(false)
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
        onDismissRequest = { signState.bottomSheetState.closeBottomSheet() },
        sheetState = signState.bottomSheetState.sheetState,
        modifier = modifier,
        openBottomSheet = signState.bottomSheetState.isShowBottomSheet,
    ) {

        SectionOption(title = "Options") {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SMIconButton(
                    imageVector = Icons.Default.CleaningServices,
                    label = "Clear",
                    onClick = {
                        signState.clearFunction?.invoke()
                        signState.bottomSheetState.closeBottomSheet()
                    },
                )

                SMIconButton(
                    imageVector = Icons.Default.Save,
                    label = "Save",
                    onClick = { signState.bottomSheetState.closeBottomSheet() },
                )

                SMIconButton(
                    imageVector = Icons.Default.Share,
                    label = "Share",
                    onClick = { signState.bottomSheetState.closeBottomSheet() },
                )
            }
        }


        SectionOption(title = "Stroke color") {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                colorPen.forEach { color ->
                    SMColorSelector(
                        color = color,
                        selected = color == signState.strokeColorState.selectedColor,
                        modifier = Modifier.size(SMTheme.size.size450),
                        onClick = { signState.strokeColorState.changeColor(color) }
                    )
                }
            }
        }

        SectionOption(title = "Stroke width") {

            var sliderValues by remember {
                mutableStateOf(5f..13f) // pass the initial values
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RangeSlider(
                    value = sliderValues,
                    onValueChange = { newValues ->
                        sliderValues = newValues
                    },
                    onValueChangeFinished = {},
                    valueRange = 1f..20f,
                    steps = 0
                )
                SMText(text = "Start: ${sliderValues.start}, End: ${sliderValues.endInclusive}")
            }
        }

        SectionOption(title = "Background") {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                imageBackground.forEach { image ->
                    SMImageSelector(
                        image = painterResource(id = image),
                        selected = image == signState.backgroundState.selectedImage,
                        modifier = Modifier.size(SMTheme.size.size450),
                        onClick = { signState.backgroundState.changeImage(image) }
                    )
                }
            }
        }
    }
}

@Composable
fun SectionOption(
    title: String, modifier: Modifier = Modifier, content: @Composable () -> Unit
) {
    Column(modifier = modifier.padding(horizontal = SMTheme.spacing.spacing150)) {
        SMText(
            text = title, style = SMTheme.material.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(SMTheme.spacing.spacing100))
        SMLineSeparator(modifier = Modifier.padding(top = SMTheme.spacing.spacing250))
        Spacer(modifier = Modifier.height(SMTheme.spacing.spacing200))
        content()
        Spacer(modifier = Modifier.height(SMTheme.spacing.spacing200))

    }
}

@Composable
fun tiledBackgroundBrush(@DrawableRes imageRes: Int): Brush {
    val imageBitmap = ImageBitmap.imageResource(LocalContext.current.resources, imageRes)
    val shader = ImageShader(imageBitmap, TileMode.Repeated, TileMode.Repeated)
    return ShaderBrush(shader)
}
