package com.signaturemaker.app.application.ui.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.signaturemaker.app.application.ui.designsystem.SMTheme

/**
 * A line separator component.
 */
@Composable
fun SMLineSeparator(
    modifier: Modifier = Modifier,
    backgroundColor: Color = SMTheme.material.colorScheme.outlineVariant
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(SMTheme.size.size10)
            .background(backgroundColor)
    )
}
