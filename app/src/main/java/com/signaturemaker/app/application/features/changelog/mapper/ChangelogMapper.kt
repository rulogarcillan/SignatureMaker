package com.signaturemaker.app.application.features.changelog.mapper

import com.signaturemaker.app.application.features.changelog.model.ChangeType
import com.signaturemaker.app.application.features.changelog.model.ChangeUI
import com.signaturemaker.app.application.features.changelog.model.ChangelogUI
import com.signaturemaker.app.domain.models.Change
import com.signaturemaker.app.domain.models.Changelog
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * Mapper to convert domain Changelog models to UI models
 */
object ChangelogMapper {

    /**
     * Convert domain Changelog to UI ChangelogUI
     */
    fun Changelog.toUI(): ChangelogUI {
        return ChangelogUI(
            versionCode = this.versionCode,
            versionName = this.versionName,
            date = this.date,
            changes = this.change.map { it.toUI() }.toImmutableList()
        )
    }

    /**
     * Convert domain Change to UI ChangeUI
     */
    fun Change.toUI(): ChangeUI {
        return ChangeUI(
            type = ChangeType.fromString(this.type),
            text = this.text
        )
    }

    /**
     * Convert list of domain Changelogs to UI ChangelogUI
     */
    fun List<Changelog>.toUI(): ImmutableList<ChangelogUI> {
        return this.map { it.toUI() }.toImmutableList()
    }
}
