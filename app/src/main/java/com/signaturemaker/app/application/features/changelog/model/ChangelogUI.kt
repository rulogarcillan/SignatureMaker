package com.signaturemaker.app.application.features.changelog.model

import kotlinx.collections.immutable.ImmutableList

/**
 * UI model for Changelog - optimized for Compose
 */
data class ChangelogUI(
    val versionCode: Int,
    val versionName: String,
    val date: String?,
    val changes: ImmutableList<ChangeUI>
)

/**
 * UI model for individual Change - optimized for Compose
 */
data class ChangeUI(
    val type: ChangeType,
    val text: String
)

