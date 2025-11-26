package com.signaturemaker.app.application.ui.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.signaturemaker.app.application.ui.designsystem.SMTheme

/**
 * A line separator component.
 */
@Composable
fun SMLineSeparator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(SMTheme.size.size10)
            .background(SMTheme.material.colorScheme.outlineVariant)
    )
}
