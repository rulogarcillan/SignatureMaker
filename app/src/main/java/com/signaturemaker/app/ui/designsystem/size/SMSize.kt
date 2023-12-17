package com.signaturemaker.app.ui.designsystem.size

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import com.signaturemaker.app.ui.designsystem.Foundation


@Immutable
private object SMSizeDefaults {
  val Size00: Dp = Foundation.size0
  val Size50: Dp = Foundation.size4
  val Size100: Dp = Foundation.size8
  val Size125: Dp = Foundation.size10
  val Size150: Dp = Foundation.size12
  val Size200: Dp = Foundation.size16
  val Size250: Dp = Foundation.size20
  val Size300: Dp = Foundation.size24
  val Size350: Dp = Foundation.size32
  val Size375: Dp = Foundation.size34
  val Size400: Dp = Foundation.size40
  val Size425: Dp = Foundation.size44
  val Size450: Dp = Foundation.size48
  val Size500: Dp = Foundation.size56
  val Size550: Dp = Foundation.size64
  val Size600: Dp = Foundation.size72
  val Size650: Dp = Foundation.size80
  val Size675: Dp = Foundation.size92
  val Size700: Dp = Foundation.size96
  val Size750: Dp = Foundation.size104
  val Size800: Dp = Foundation.size112
  val Size850: Dp = Foundation.size120
  val Size900: Dp = Foundation.size128
  val Size950: Dp = Foundation.size136
  val Size975: Dp = Foundation.size140
  val Size1000: Dp = Foundation.size144
  val Size1050: Dp = Foundation.size152
  val Size1075: Dp = Foundation.size156
  val Size1083: Dp = Foundation.size194
  val Size1088: Dp = Foundation.size160
  val Size1089: Dp = Foundation.size164
  val Size1091: Dp = Foundation.size195
  val Size1100: Dp = Foundation.size200
  val Size1125: Dp = Foundation.size214
  val Size1150: Dp = Foundation.size238
  val Size1175: Dp = Foundation.size239
  val Size1200: Dp = Foundation.size328
}

@Immutable
data class SMSize(
  val size00: Dp = SMSizeDefaults.Size00,
  val size50: Dp = SMSizeDefaults.Size50,
  val size100: Dp = SMSizeDefaults.Size100,
  val size125: Dp = SMSizeDefaults.Size125,
  val size150: Dp = SMSizeDefaults.Size150,
  val size200: Dp = SMSizeDefaults.Size200,
  val size250: Dp = SMSizeDefaults.Size250,
  val size300: Dp = SMSizeDefaults.Size300,
  val size350: Dp = SMSizeDefaults.Size350,
  val size375: Dp = SMSizeDefaults.Size375,
  val size400: Dp = SMSizeDefaults.Size400,
  val size425: Dp = SMSizeDefaults.Size425,
  val size450: Dp = SMSizeDefaults.Size450,
  val size500: Dp = SMSizeDefaults.Size500,
  val size550: Dp = SMSizeDefaults.Size550,
  val size600: Dp = SMSizeDefaults.Size600,
  val size650: Dp = SMSizeDefaults.Size650,
  val size675: Dp = SMSizeDefaults.Size675,
  val size700: Dp = SMSizeDefaults.Size700,
  val size750: Dp = SMSizeDefaults.Size750,
  val size800: Dp = SMSizeDefaults.Size800,
  val size850: Dp = SMSizeDefaults.Size850,
  val size900: Dp = SMSizeDefaults.Size900,
  val size950: Dp = SMSizeDefaults.Size950,
  val size975: Dp = SMSizeDefaults.Size975,
  val size1000: Dp = SMSizeDefaults.Size1000,
  val size1050: Dp = SMSizeDefaults.Size1050,
  val size1075: Dp = SMSizeDefaults.Size1075,
  val size1083: Dp = SMSizeDefaults.Size1083,
  val size1088: Dp = SMSizeDefaults.Size1088,
  val size1089: Dp = SMSizeDefaults.Size1089,
  val size1091: Dp = SMSizeDefaults.Size1091,
  val size1100: Dp = SMSizeDefaults.Size1100,
  val size1125: Dp = SMSizeDefaults.Size1125,
  val size1150: Dp = SMSizeDefaults.Size1150,
  val size1175: Dp = SMSizeDefaults.Size1175,
  val size1200: Dp = SMSizeDefaults.Size1200,
)

internal val LocalSMSize: ProvidableCompositionLocal<SMSize> = staticCompositionLocalOf { SMSize() }
