package com.signaturemaker.app.application.features.files

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FilesRoute(
    modifier: Modifier = Modifier,
    viewModel: FilesViewModel = org.koin.androidx.compose.koinViewModel()
) {
    FilesScreen(
        viewModel = viewModel,
        modifier = modifier
    )
}
