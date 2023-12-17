package com.signaturemaker.app.ui.designsystem.spacing

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import com.signaturemaker.app.ui.designsystem.Foundation

@Immutable
private object SMSpacingDefaults {
  val Spacing00: Dp = Foundation.spacing0
  val Spacing25: Dp = Foundation.spacing1
  val Spacing50: Dp = Foundation.spacing2
  val Spacing100: Dp = Foundation.spacing4
  val Spacing150: Dp = Foundation.spacing8
  val Spacing200: Dp = Foundation.spacing12
  val Spacing250: Dp = Foundation.spacing16
  val Spacing300: Dp = Foundation.spacing24
  val Spacing350: Dp = Foundation.spacing32
  val Spacing400: Dp = Foundation.spacing40
  val Spacing450: Dp = Foundation.spacing48
  val Spacing500: Dp = Foundation.spacing56
  val Spacing550: Dp = Foundation.spacing64
  val Spacing575: Dp = Foundation.spacing120
  val Spacing600: Dp = Foundation.spacing128
}

@Immutable
data class SMSpacing(
  val spacing00: Dp = SMSpacingDefaults.Spacing00,
  val spacing25: Dp = SMSpacingDefaults.Spacing25,
  val spacing50: Dp = SMSpacingDefaults.Spacing50,
  val spacing100: Dp = SMSpacingDefaults.Spacing100,
  val spacing150: Dp = SMSpacingDefaults.Spacing150,
  val spacing200: Dp = SMSpacingDefaults.Spacing200,
  val spacing250: Dp = SMSpacingDefaults.Spacing250,
  val spacing300: Dp = SMSpacingDefaults.Spacing300,
  val spacing350: Dp = SMSpacingDefaults.Spacing350,
  val spacing400: Dp = SMSpacingDefaults.Spacing400,
  val spacing450: Dp = SMSpacingDefaults.Spacing450,
  val spacing500: Dp = SMSpacingDefaults.Spacing500,
  val spacing550: Dp = SMSpacingDefaults.Spacing550,
  val spacing575: Dp = SMSpacingDefaults.Spacing575,
  val spacing600: Dp = SMSpacingDefaults.Spacing600
)

internal val LocalSMSpacing: ProvidableCompositionLocal<SMSpacing> = staticCompositionLocalOf {
  SMSpacing()
}
