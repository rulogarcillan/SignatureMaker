package com.signaturemaker.app.application.features.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.application.utils.Constants
import com.tuppersoft.skizo.android.core.extension.loadSharedPreference
import com.tuppersoft.skizo.android.core.extension.saveSharedPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUIState())
    val uiState: StateFlow<SettingsUIState> = _uiState.asStateFlow()

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    deleteOnExit = context.loadSharedPreference(
                        Constants.ID_PREF_DELETE,
                        Constants.DEFAULT_DELETE_EXIT
                    ),
                    rememberColor = context.loadSharedPreference(
                        Constants.ID_PREF_COLOR,
                        false
                    ),
                    rememberStroke = context.loadSharedPreference(
                        Constants.ID_PREF_STROKE,
                        false
                    ),
                    rememberWallpaper = context.loadSharedPreference(
                        Constants.ID_PREF_WALLPAPER,
                        false
                    )
                )
            }
        }
    }

    fun onDeleteOnExitChanged(enabled: Boolean) {
        viewModelScope.launch {
            context.saveSharedPreference(Constants.ID_PREF_DELETE, enabled)
            _uiState.update { it.copy(deleteOnExit = enabled) }
            Utils.deleteExit = enabled
        }
    }

    fun onRememberColorChanged(enabled: Boolean) {
        viewModelScope.launch {
            context.saveSharedPreference(Constants.ID_PREF_COLOR, enabled)
            _uiState.update { it.copy(rememberColor = enabled) }

            if (enabled) {
                context.saveSharedPreference(Constants.PREF_COLOR, Utils.penColor)
            } else {
                Utils.penColor = Constants.DEFAULT_PEN_COLOR
            }
        }
    }

    fun onRememberStrokeChanged(enabled: Boolean) {
        viewModelScope.launch {
            context.saveSharedPreference(Constants.ID_PREF_STROKE, enabled)
            _uiState.update { it.copy(rememberStroke = enabled) }

            if (enabled) {
                context.saveSharedPreference(Constants.PREF_MIN_TROKE, Utils.minStroke)
                context.saveSharedPreference(Constants.PREF_MAX_TROKE, Utils.maxStroke)
            } else {
                Utils.minStroke = Constants.DEFAULT_MIN_STROKE
                Utils.maxStroke = Constants.DEFAULT_MAX_STROKE
            }
        }
    }

    fun onRememberWallpaperChanged(enabled: Boolean) {
        viewModelScope.launch {
            context.saveSharedPreference(Constants.ID_PREF_WALLPAPER, enabled)
            _uiState.update { it.copy(rememberWallpaper = enabled) }

            if (enabled) {
                context.saveSharedPreference(Constants.PREF_WALLPAPER, Utils.wallpaper)
            } else {
                Utils.wallpaper = Constants.DEFAULT_WALLPAPER
            }
        }
    }

    fun onResetToDefaults() {
        viewModelScope.launch {
            // Reset all preferences to defaults
            context.saveSharedPreference(Constants.ID_PREF_DELETE, false)
            context.saveSharedPreference(Constants.ID_PREF_COLOR, false)
            context.saveSharedPreference(Constants.ID_PREF_STROKE, false)
            context.saveSharedPreference(Constants.ID_PREF_WALLPAPER, false)

            // Update UI state
            _uiState.update {
                SettingsUIState(
                    deleteOnExit = false,
                    rememberColor = false,
                    rememberStroke = false,
                    rememberWallpaper = false
                )
            }

            // Reset Utils values
            Utils.defaultValues()
            Utils.saveAllPreferences(context)
        }
    }
}

