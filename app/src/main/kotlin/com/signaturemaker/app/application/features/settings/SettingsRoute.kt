package com.signaturemaker.app.application.features.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel()
) {
    SettingsScreen(
        modifier = modifier,
        viewModel = viewModel
    )
}
