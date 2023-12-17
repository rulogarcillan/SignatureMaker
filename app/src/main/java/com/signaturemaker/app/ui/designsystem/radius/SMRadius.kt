package com.signaturemaker.app.ui.designsystem.radius

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.signaturemaker.app.ui.designsystem.Foundation

@Immutable
private object SMRadiusDefaults {
  val Radius00: Dp = Foundation.radius0
  val Radius50: Dp = Foundation.radius2
  val Radius100: Dp = Foundation.radius4
  val Radius150: Dp = Foundation.radius8
  val Radius200: Dp = Foundation.radius12
  val Radius250: Dp = Foundation.radius16
  val Radius275: Dp = Foundation.radius17
  val Radius300: Dp = Foundation.radius24
  val Radius1000: Dp = Foundation.radius360
}

@Immutable
data class SMRadius(
  val radius00: Dp = SMRadiusDefaults.Radius00,
  val radius50: Dp = SMRadiusDefaults.Radius50,
  val radius100: Dp = SMRadiusDefaults.Radius100,
  val radius150: Dp = SMRadiusDefaults.Radius150,
  val radius200: Dp = SMRadiusDefaults.Radius200,
  val radius250: Dp = SMRadiusDefaults.Radius250,
  val radius275: Dp = SMRadiusDefaults.Radius275,
  val radius300: Dp = SMRadiusDefaults.Radius300,
  val radius1000: Dp = SMRadiusDefaults.Radius1000
) {
  val roundedCornerShape00: Shape = RoundedCornerShape(radius00)
  val roundedCornerShape50: Shape = RoundedCornerShape(radius50)
  val roundedCornerShape100: Shape = RoundedCornerShape(radius100)
  val roundedCornerShape150: Shape = RoundedCornerShape(radius150)
  val roundedCornerShape200: Shape = RoundedCornerShape(radius200)
  val roundedCornerShape250: Shape = RoundedCornerShape(radius250)
  val roundedCornerShape275: Shape = RoundedCornerShape(radius275)
  val roundedCornerShape300: Shape = RoundedCornerShape(radius300)
  val roundedCornerShape1000: Shape = RoundedCornerShape(radius1000)
}

internal val LocalSMRadius: ProvidableCompositionLocal<SMRadius> = staticCompositionLocalOf {
  SMRadius()
}
