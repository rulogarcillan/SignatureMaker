package com.signaturemaker.app.application.ui.theming

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.signaturemaker.app.application.ui.designsystem.SMTheme
import com.signaturemaker.app.application.ui.theming.color.DarkColor
import com.signaturemaker.app.application.ui.theming.color.DarkSmColor
import com.signaturemaker.app.application.ui.theming.color.LightColor
import com.signaturemaker.app.application.ui.theming.color.LightSmColor

/**
 * Application theme wrapper
 */
@Composable
fun SignatureMakerAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val materialColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColor
        else -> LightColor
    }

    val smColor = when {
        darkTheme -> DarkSmColor
        else -> LightSmColor
    }

    // Configurar colores de la system UI automáticamente
    val view = LocalView.current
    SideEffect {
        val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect

        // Calcular luminancia para determinar si usar iconos oscuros o claros
        // Si el color es claro (luminancia > 0.5), usar iconos oscuros para contraste

        val primaryLuminance = materialColorScheme.primary.luminance()
        val useDarkIcons = primaryLuminance > 0.5f

        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = useDarkIcons
            isAppearanceLightNavigationBars = useDarkIcons
        }
    }

    SMTheme(
        color = smColor,
        colorScheme = materialColorScheme
    ) {
        content()
    }
}
