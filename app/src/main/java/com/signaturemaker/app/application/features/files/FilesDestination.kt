package com.signaturemaker.app.application.features.files

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val FILES_ROUTE = "filescreen"

fun NavGraphBuilder.filesScreen() {
    composable(route = FILES_ROUTE) {
        FilesRoute()
    }
}

