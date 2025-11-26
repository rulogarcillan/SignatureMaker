package com.signaturemaker.app.application.ui.theming.color

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.signaturemaker.app.application.ui.designsystem.*
import com.signaturemaker.app.application.ui.designsystem.Foundation.backgroundDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.backgroundDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.backgroundDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.backgroundLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.backgroundLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.backgroundLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.errorContainerDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.errorContainerDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.errorContainerDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.errorContainerLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.errorContainerLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.errorContainerLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.errorDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.errorDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.errorDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.errorLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.errorLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.errorLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.inverseOnSurfaceDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.inverseOnSurfaceDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.inverseOnSurfaceDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.inverseOnSurfaceLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.inverseOnSurfaceLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.inverseOnSurfaceLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.inversePrimaryDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.inversePrimaryDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.inversePrimaryDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.inversePrimaryLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.inversePrimaryLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.inversePrimaryLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.inverseSurfaceDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.inverseSurfaceDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.inverseSurfaceDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.inverseSurfaceLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.inverseSurfaceLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.inverseSurfaceLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onBackgroundDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.onBackgroundDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onBackgroundDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onBackgroundLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.onBackgroundLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onBackgroundLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onErrorContainerDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.onErrorContainerDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onErrorContainerDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onErrorContainerLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.onErrorContainerLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onErrorContainerLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onErrorDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.onErrorDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onErrorDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onErrorLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.onErrorLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onErrorLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onPrimaryContainerDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.onPrimaryContainerDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onPrimaryContainerDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onPrimaryContainerLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.onPrimaryContainerLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onPrimaryContainerLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onPrimaryDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.onPrimaryDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onPrimaryDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onPrimaryLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.onPrimaryLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onPrimaryLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSecondaryContainerDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSecondaryContainerDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSecondaryContainerDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSecondaryContainerLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSecondaryContainerLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSecondaryContainerLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSecondaryDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSecondaryDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSecondaryDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSecondaryLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSecondaryLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSecondaryLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSurfaceDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSurfaceDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSurfaceDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSurfaceLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSurfaceLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSurfaceLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSurfaceVariantDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSurfaceVariantDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSurfaceVariantDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSurfaceVariantLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSurfaceVariantLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onSurfaceVariantLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onTertiaryContainerDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.onTertiaryContainerDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onTertiaryContainerDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onTertiaryContainerLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.onTertiaryContainerLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onTertiaryContainerLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onTertiaryDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.onTertiaryDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onTertiaryDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onTertiaryLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.onTertiaryLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.onTertiaryLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.outlineDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.outlineDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.outlineDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.outlineLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.outlineLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.outlineLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.outlineVariantDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.outlineVariantDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.outlineVariantDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.outlineVariantLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.outlineVariantLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.outlineVariantLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.primaryContainerDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.primaryContainerDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.primaryContainerDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.primaryContainerLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.primaryContainerLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.primaryContainerLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.primaryDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.primaryDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.primaryDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.primaryLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.primaryLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.primaryLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.scrimDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.scrimDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.scrimDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.scrimLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.scrimLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.scrimLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.secondaryContainerDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.secondaryContainerDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.secondaryContainerDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.secondaryContainerLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.secondaryContainerLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.secondaryContainerLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.secondaryDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.secondaryDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.secondaryDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.secondaryLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.secondaryLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.secondaryLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceBrightDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceBrightDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceBrightDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceBrightLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceBrightLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceBrightLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerHighDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerHighDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerHighDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerHighLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerHighLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerHighLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerHighestDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerHighestDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerHighestDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerHighestLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerHighestLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerHighestLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerLowDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerLowDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerLowDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerLowLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerLowLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerLowLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerLowestDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerLowestDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerLowestDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerLowestLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerLowestLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceContainerLowestLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceDimDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceDimDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceDimDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceDimLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceDimLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceDimLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceVariantDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceVariantDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceVariantDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceVariantLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceVariantLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.surfaceVariantLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.tertiaryContainerDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.tertiaryContainerDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.tertiaryContainerDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.tertiaryContainerLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.tertiaryContainerLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.tertiaryContainerLightMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.tertiaryDark
import com.signaturemaker.app.application.ui.designsystem.Foundation.tertiaryDarkHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.tertiaryDarkMediumContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.tertiaryLight
import com.signaturemaker.app.application.ui.designsystem.Foundation.tertiaryLightHighContrast
import com.signaturemaker.app.application.ui.designsystem.Foundation.tertiaryLightMediumContrast

 val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

 val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

 val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

 val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

 val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

