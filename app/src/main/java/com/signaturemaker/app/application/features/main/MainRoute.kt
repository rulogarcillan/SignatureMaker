package com.signaturemaker.app.application.features.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainRoute(
    modifier: Modifier = Modifier,
    onNavigationAction: (MainScreenAction) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    MainScreen(
        modifier = modifier,
        onNavigationAction = onNavigationAction,
        content = content
    )
}

