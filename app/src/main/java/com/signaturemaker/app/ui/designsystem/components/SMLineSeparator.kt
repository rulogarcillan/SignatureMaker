package com.signaturemaker.app.ui.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.signaturemaker.app.ui.designsystem.SMTheme

@Composable
fun SMLineSeparator(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(SMTheme.size.size10)
            .background(SMTheme.material.colorScheme.onSurface.copy(alpha = 0.10f))
    )
}