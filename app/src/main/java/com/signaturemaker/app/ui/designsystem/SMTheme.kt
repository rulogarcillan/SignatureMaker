package com.signaturemaker.app.ui.designsystem

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import com.signaturemaker.app.ui.designsystem.color.LocalSMColor
import com.signaturemaker.app.ui.designsystem.color.SMColor
import com.signaturemaker.app.ui.designsystem.radius.LocalSMRadius
import com.signaturemaker.app.ui.designsystem.radius.SMRadius
import com.signaturemaker.app.ui.designsystem.size.LocalSMSize
import com.signaturemaker.app.ui.designsystem.size.SMSize
import com.signaturemaker.app.ui.designsystem.spacing.LocalSMSpacing
import com.signaturemaker.app.ui.designsystem.spacing.SMSpacing
import com.signaturemaker.app.ui.theming.typografy.Typography

/**
 * SM Design System Theme wrapper
 */
@Composable
fun SMTheme(
    radius: SMRadius = SMTheme.radius,
    size: SMSize = SMTheme.size,
    spacing: SMSpacing = SMTheme.spacing,
    color: SMColor = SMTheme.color,
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalSMRadius provides radius,
        LocalSMColor provides color,
        LocalSMSize provides size,
        LocalSMSpacing provides spacing,
        LocalMinimumInteractiveComponentSize provides 0.dp,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
        ) {
            content()
        }
    }
}

/**
 * SM Design System Theme
 */
object SMTheme {
    val radius: SMRadius
        @Composable
        get() = LocalSMRadius.current

    val size: SMSize
        @Composable
        get() = LocalSMSize.current

    val spacing: SMSpacing
        @Composable
        get() = LocalSMSpacing.current

    val material: MaterialTheme
        @Composable
        get() = MaterialTheme

    val color: SMColor
        @Composable
        get() = LocalSMColor.current
}
