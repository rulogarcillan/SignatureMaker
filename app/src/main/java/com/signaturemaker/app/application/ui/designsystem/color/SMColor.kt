package com.signaturemaker.app.application.ui.designsystem.color

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.signaturemaker.app.application.ui.designsystem.Foundation

@Immutable
private object SMColorDefaults {
  val Pen1: Color = Foundation.Blue
  val Pen2: Color = Foundation.Black
  val Pen3: Color = Foundation.Red
  val Pen4: Color = Foundation.Green
  val BackgroundSheet : Color = Foundation.White
  val BackgroundTextSheet : Color = Foundation.Black
}

/**
 * Color palette for SignatureMaker app
 */
@Immutable
data class SMColor(
    val pen1: Color = SMColorDefaults.Pen1,
    val pen2: Color = SMColorDefaults.Pen2,
    val pen3: Color = SMColorDefaults.Pen3,
    val pen4: Color = SMColorDefaults.Pen4,
    val backgroundSheet: Color = SMColorDefaults.BackgroundSheet,
    val backgroundTextSheet: Color = SMColorDefaults.BackgroundTextSheet
)

internal val LocalSMColor: ProvidableCompositionLocal<SMColor> = staticCompositionLocalOf {
  SMColor()
}
