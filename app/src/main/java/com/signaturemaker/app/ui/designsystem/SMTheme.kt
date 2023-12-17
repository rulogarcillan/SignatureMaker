package com.signaturemaker.app.ui.designsystem

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.signaturemaker.app.ui.designsystem.radius.LocalSMRadius
import com.signaturemaker.app.ui.designsystem.radius.SMRadius
import com.signaturemaker.app.ui.designsystem.size.LocalSMSize
import com.signaturemaker.app.ui.designsystem.size.SMSize
import com.signaturemaker.app.ui.designsystem.spacing.LocalSMSpacing
import com.signaturemaker.app.ui.designsystem.spacing.SMSpacing
import com.signaturemaker.app.ui.theming.typografy.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SMTheme(
    radius: SMRadius = SMTheme.radius,
    size: SMSize = SMTheme.size,
    spacing: SMSpacing = SMTheme.spacing,
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    content: @Composable () -> Unit
) {

    CompositionLocalProvider(
        LocalSMRadius provides radius,
        LocalSMSize provides size,
        LocalSMSpacing provides spacing,
        LocalMinimumInteractiveComponentEnforcement provides false,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
        ) {
            content()
        }
    }
}

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
}