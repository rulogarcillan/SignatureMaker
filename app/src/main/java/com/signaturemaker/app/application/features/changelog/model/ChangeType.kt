package com.signaturemaker.app.application.features.changelog.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Science
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Enum representing different types of changes in a changelog
 */
enum class ChangeType(
    val displayName: String,
    val icon: ImageVector
) {
    FIX(
        displayName = "Fix",
        icon = Icons.Filled.BugReport
    ),
    FEAT(
        displayName = "Feature",
        icon = Icons.Filled.NewReleases
    ),
    REFACTOR(
        displayName = "Refactor",
        icon = Icons.Filled.AutoFixHigh
    ),
    DOCS(
        displayName = "Docs",
        icon = Icons.AutoMirrored.Filled.Article
    ),
    TEST(
        displayName = "Test",
        icon = Icons.Filled.Science
    ),
    CHORE(
        displayName = "Chore",
        icon = Icons.Filled.Construction
    ),
    DEL(
        displayName = "Removed",
        icon = Icons.Filled.DeleteForever
    );

    companion object {
        /**
         * Parse a string type from JSON to ChangeType enum
         * Handles various formats: "feat", "FEAT", "feature", "fix", "FIX", etc.
         */
        fun fromString(type: String): ChangeType {
            return when (type.lowercase().trim()) {
                "fix", "fixed", "bugfix" -> FIX
                "feat", "feature", "new", "added" -> FEAT
                "refactor", "improvement", "improved", "update" -> REFACTOR
                "docs", "doc", "documentation" -> DOCS
                "test", "tests" -> TEST
                "chore", "task" -> CHORE
                "del", "delete", "removed", "remove" -> DEL
                else -> FEAT // Default to FEAT for unknown types
            }
        }
    }
}
