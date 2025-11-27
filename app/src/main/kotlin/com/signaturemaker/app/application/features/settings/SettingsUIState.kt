package com.signaturemaker.app.application.features.settings

import androidx.compose.runtime.Immutable

@Immutable
data class SettingsUIState(
    val deleteOnExit: Boolean = false,
    val rememberColor: Boolean = false,
    val rememberStroke: Boolean = false,
    val rememberWallpaper: Boolean = false
)
