package com.signaturemaker.app.application.features.changelog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.signaturemaker.app.application.features.changelog.model.ChangeUI
import com.signaturemaker.app.application.features.changelog.model.ChangelogUI
import com.signaturemaker.app.application.ui.designsystem.SMTheme
import com.signaturemaker.app.application.ui.designsystem.components.SMIcon
import com.signaturemaker.app.application.ui.designsystem.components.SMText
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel


// ============================================
// MAIN SCREEN
// ============================================

@Composable
fun ChangelogScreen(
    modifier: Modifier = Modifier,
    viewModel: ChangelogViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ChangelogContent(
        modifier = modifier.fillMaxSize(),
        changelogs = uiState.changelogs
    )
}

@Composable
private fun ChangelogContent(
    modifier: Modifier = Modifier,
    changelogs: ImmutableList<ChangelogUI>
) {
    if (changelogs.isEmpty()) {
        EmptyChangelogState(modifier = modifier)
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(
                horizontal = SMTheme.spacing.spacing200,
                vertical = SMTheme.spacing.spacing150
            ),
            verticalArrangement = Arrangement.spacedBy(SMTheme.spacing.spacing150)
        ) {
            items(
                items = changelogs,
                key = { it.versionCode }
            ) { changelog ->
                ChangelogVersionCard(
                    changelog = changelog.sortChangesByType()
                )
            }
        }
    }
}

@Composable
private fun EmptyChangelogState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(SMTheme.spacing.spacing400),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            modifier = Modifier.size(SMTheme.size.size600),
            tint = SMTheme.material.colorScheme.outline.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(SMTheme.spacing.spacing250))

        SMText(
            text = "No changelog available",
            style = SMTheme.material.typography.titleMedium,
            color = SMTheme.material.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(SMTheme.spacing.spacing100))

        SMText(
            text = "Check back later for updates",
            style = SMTheme.material.typography.bodyMedium,
            color = SMTheme.material.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun ChangelogVersionCard(
    changelog: ChangelogUI,
    modifier: Modifier = Modifier
) {
    var isExpanded by rememberSaveable(key = "changelog_${changelog.versionCode}") {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(SMTheme.radius.radius150),
        colors = CardDefaults.cardColors(
             containerColor = SMTheme.material.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = SMTheme.size.size00
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(SMTheme.spacing.spacing150)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    SMText(
                        text = "Version ${changelog.versionName}",
                        style = SMTheme.material.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SMTheme.material.colorScheme.onSurface
                    )

                    changelog.date?.let { date ->
                        Spacer(modifier = Modifier.height(SMTheme.spacing.spacing50))
                        SMText(
                            text = formatDate(date),
                            style = SMTheme.material.typography.bodySmall,
                            color = SMTheme.material.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = SMTheme.material.colorScheme.secondaryContainer
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        modifier = Modifier.padding(SMTheme.spacing.spacing100),
                        tint = SMTheme.material.colorScheme.onSecondaryContainer
                    )
                }
            }

            // Expandable content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = SMTheme.spacing.spacing150)
                ) {
                    changelog.changes.forEach { change ->
                        ChangeItem(
                            change = change,
                            modifier = Modifier.padding(bottom = SMTheme.spacing.spacing250)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChangeItem(
    change: ChangeUI,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Icon based on type from enum
        Surface(
            shape = RoundedCornerShape(SMTheme.radius.radius100),
            color = SMTheme.material.colorScheme.secondaryContainer,
            modifier = Modifier.padding(end = SMTheme.spacing.spacing100)
        ) {
            SMIcon(
                imageVector = change.type.icon,
                contentDescription = change.type.displayName,
                modifier = Modifier
                    .padding(SMTheme.spacing.spacing100)
                    .size(SMTheme.size.size200)
            )
        }

        Spacer(modifier = Modifier.width(SMTheme.spacing.spacing100))

        // Change text
        Column(modifier = Modifier.weight(1f)) {
            SMText(
                text = change.type.displayName.uppercase(Locale.getDefault()),
                style = SMTheme.material.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = SMTheme.material.colorScheme.primary,
                modifier = Modifier.padding(bottom = SMTheme.spacing.spacing50)
            )
            SMText(
                text = change.text,
                style = SMTheme.material.typography.bodyMedium,
                color = SMTheme.material.colorScheme.onSurface
            )
        }
    }
}


private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}

/**
 * Extension function to sort changes by type priority
 * Order: FEAT -> REFACTOR -> FIX -> DOCS -> TEST -> CHORE -> DEL
 */
private fun ChangelogUI.sortChangesByType(): ChangelogUI {
    val sortedChanges = changes.sortedBy { it.type.getPriority() }
    return copy(changes = sortedChanges.toImmutableList())
}

/**
 * Get priority order for change types
 * Lower number = higher priority
 */
private fun com.signaturemaker.app.application.features.changelog.model.ChangeType.getPriority(): Int {
    return when (this) {
        com.signaturemaker.app.application.features.changelog.model.ChangeType.FEAT -> 1
        com.signaturemaker.app.application.features.changelog.model.ChangeType.REFACTOR -> 2
        com.signaturemaker.app.application.features.changelog.model.ChangeType.FIX -> 3
        com.signaturemaker.app.application.features.changelog.model.ChangeType.DOCS -> 4
        com.signaturemaker.app.application.features.changelog.model.ChangeType.TEST -> 5
        com.signaturemaker.app.application.features.changelog.model.ChangeType.CHORE -> 6
        com.signaturemaker.app.application.features.changelog.model.ChangeType.DEL -> 7
    }
}

