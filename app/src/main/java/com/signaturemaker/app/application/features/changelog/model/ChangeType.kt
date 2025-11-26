package com.signaturemaker.app.application.features.changelog.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Enum representing different types of changes in a changelog
 */
enum class ChangeType(
    val displayName: String,
    val icon: ImageVector,
    val color: Color
) {
    FIX(
        displayName = "Fix",
        icon = Icons.Default.Build,
        color = Color(0xFF2196F3) // Blue
    ),
    FEAT(
        displayName = "Feature",
        icon = Icons.Default.Add,
        color = Color(0xFF4CAF50) // Green
    ),
    REFACTOR(
        displayName = "Refactor",
        icon = Icons.Default.Settings,
        color = Color(0xFFFF9800) // Orange
    ),
    DOCS(
        displayName = "Docs",
        icon = Icons.Default.CheckCircle,
        color = Color(0xFF9C27B0) // Purple
    ),
    TEST(
        displayName = "Test",
        icon = Icons.Default.CheckCircle,
        color = Color(0xFF00BCD4) // Cyan
    ),
    CHORE(
        displayName = "Chore",
        icon = Icons.Default.Settings,
        color = Color(0xFF607D8B) // Blue Grey
    ),
    DEL(
        displayName = "Removed",
        icon = Icons.Default.CheckCircle,
        color = Color(0xFFF44336) // Red
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

