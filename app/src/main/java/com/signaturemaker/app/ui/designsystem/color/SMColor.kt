package com.signaturemaker.app.ui.designsystem.color

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.signaturemaker.app.ui.designsystem.Foundation

@Immutable
private object SMColorDefaults {
  val Pen1: Color = Foundation.Blue40
  val Pen2: Color = Foundation.Black40
  val Pen3: Color = Foundation.Red40
  val Pen4: Color = Foundation.Green40
  val Selector: Color = Foundation.White40
  val Selector2: Color = Foundation.Gray40

}

@Immutable
data class SMColor(
  val pen1: Color = SMColorDefaults.Pen1,
  val pen2: Color = SMColorDefaults.Pen2,
  val pen3: Color = SMColorDefaults.Pen3,
  val pen4: Color = SMColorDefaults.Pen4,
  val selector: Color = SMColorDefaults.Selector,
  val selector2: Color = SMColorDefaults.Selector2,
  //val border: Color = SMColorDefaults.Border,

)

internal val LocalSMColor: ProvidableCompositionLocal<SMColor> = staticCompositionLocalOf {
  SMColor()
}
