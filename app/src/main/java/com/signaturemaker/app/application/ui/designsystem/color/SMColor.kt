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
  val BackgroundSheet: Color = Foundation.White
  val BackgroundTextSheet: Color = Foundation.Black

  // Changelog Type Colors - Using Material3 palette for better contrast
  val ChangelogFix: Color = Foundation.tertiaryLight // Azul suave para arreglos
  val ChangelogFeat: Color = Foundation.primaryLight // Verde azulado principal para nuevas características
  val ChangelogRefactor: Color = Foundation.secondaryLight // Gris azulado para refactorización
  val ChangelogDocs: Color = Foundation.tertiaryDark // Azul documentación
  val ChangelogTest: Color = Foundation.primaryDark // Verde azulado oscuro para tests
  val ChangelogChore: Color = Foundation.onSurfaceVariantLight // Gris neutral para tareas
  val ChangelogDel: Color = Foundation.errorLight // Rojo para eliminaciones
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
    val backgroundTextSheet: Color = SMColorDefaults.BackgroundTextSheet,

    // Changelog Type Colors
    val changelogFix: Color = SMColorDefaults.ChangelogFix,
    val changelogFeat: Color = SMColorDefaults.ChangelogFeat,
    val changelogRefactor: Color = SMColorDefaults.ChangelogRefactor,
    val changelogDocs: Color = SMColorDefaults.ChangelogDocs,
    val changelogTest: Color = SMColorDefaults.ChangelogTest,
    val changelogChore: Color = SMColorDefaults.ChangelogChore,
    val changelogDel: Color = SMColorDefaults.ChangelogDel
)

internal val LocalSMColor: ProvidableCompositionLocal<SMColor> = staticCompositionLocalOf {
  SMColor()
}
