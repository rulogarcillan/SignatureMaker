package com.signaturemaker.app.application.features.changelog

import com.signaturemaker.app.application.features.changelog.model.ChangelogUI
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * UI State for Changelog screen
 */
data class ChangelogUIState(
    val changelogs: ImmutableList<ChangelogUI> = persistentListOf(),
    val isLoading: Boolean = false
)
