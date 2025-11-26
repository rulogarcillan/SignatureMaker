package com.signaturemaker.app.application.features.changelog

import androidx.lifecycle.ViewModel
import com.signaturemaker.app.application.features.changelog.mapper.ChangelogMapper.toUI
import com.signaturemaker.app.domain.models.Changelog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel for Changelog feature
 * Receives changelog data via dependency injection and maps to UI models
 */
class ChangelogViewModel(
    changelogList: List<Changelog>
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ChangelogUIState(changelogs = changelogList.toUI())
    )
    val uiState: StateFlow<ChangelogUIState> = _uiState.asStateFlow()
}



