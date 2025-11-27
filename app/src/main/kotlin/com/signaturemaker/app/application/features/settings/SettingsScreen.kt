package com.signaturemaker.app.application.features.settings

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.signaturemaker.app.R
import com.signaturemaker.app.application.ui.designsystem.SMTheme
import com.signaturemaker.app.application.ui.designsystem.components.SMButton
import com.signaturemaker.app.application.ui.designsystem.components.SMIcon
import com.signaturemaker.app.application.ui.designsystem.components.SMText
import com.signaturemaker.app.application.ui.snackbar.LocalSnackbarController

// ============================================
// MAIN SCREEN
// ============================================

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarController = LocalSnackbarController.current
    val context = LocalContext.current

    SettingsContent(
        modifier = modifier,
        uiState = uiState,
        onDeleteOnExitChanged = viewModel::onDeleteOnExitChanged,
        onRememberColorChanged = viewModel::onRememberColorChanged,
        onRememberStrokeChanged = viewModel::onRememberStrokeChanged,
        onRememberWallpaperChanged = viewModel::onRememberWallpaperChanged,
        onResetToDefaults = {
            viewModel.onResetToDefaults()
            snackbarController?.showInfo(
                message = context.getString(R.string.title_pref_reset_sum)
            )
        }
    )
}

@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier,
    uiState: SettingsUIState,
    onDeleteOnExitChanged: (Boolean) -> Unit,
    onRememberColorChanged: (Boolean) -> Unit,
    onRememberStrokeChanged: (Boolean) -> Unit,
    onRememberWallpaperChanged: (Boolean) -> Unit,
    onResetToDefaults: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SMTheme.material.colorScheme.background),
        contentPadding = PaddingValues(
            horizontal = SMTheme.spacing.spacing200,
            vertical = SMTheme.spacing.spacing200
        ),
        verticalArrangement = Arrangement.spacedBy(SMTheme.spacing.spacing200)
    ) {
        // Files Category
        item {
            SettingsCategoryCard(
                title = stringResource(R.string.title_pref_files),
                icon = Icons.Default.Description
            ) {
                SettingsSwitchItem(
                    title = stringResource(R.string.title_pref_delete),
                    description = stringResource(R.string.title_pref_delete_sum),
                    icon = Icons.Default.Delete,
                    checked = uiState.deleteOnExit,
                    onCheckedChange = onDeleteOnExitChanged
                )
            }
        }

        // Signature Category
        item {
            SettingsCategoryCard(
                title = stringResource(R.string.title_pref_siganture),
                icon = Icons.Default.Brush
            ) {
                SettingsSwitchItem(
                    title = stringResource(R.string.title_pref_color),
                    description = stringResource(R.string.title_pref_color_sum),
                    icon = Icons.Default.ColorLens,
                    checked = uiState.rememberColor,
                    onCheckedChange = onRememberColorChanged
                )

                Spacer(modifier = Modifier.height(SMTheme.spacing.spacing150))

                SettingsSwitchItem(
                    title = stringResource(R.string.title_pref_stroke),
                    description = stringResource(R.string.title_pref_stroke_sum),
                    icon = Icons.Default.Brush,
                    checked = uiState.rememberStroke,
                    onCheckedChange = onRememberStrokeChanged
                )

                Spacer(modifier = Modifier.height(SMTheme.spacing.spacing150))

                SettingsSwitchItem(
                    title = stringResource(R.string.title_pref_wallpaper),
                    description = stringResource(R.string.title_pref_wallpaper_sum),
                    icon = Icons.Default.Wallpaper,
                    checked = uiState.rememberWallpaper,
                    onCheckedChange = onRememberWallpaperChanged
                )
            }
        }

        // Others Category
        item {
            SettingsCategoryCard(
                title = stringResource(R.string.title_others),
                icon = Icons.Default.RestartAlt
            ) {
                SettingsActionItem(
                    title = stringResource(R.string.title_pref_reset),
                    description = stringResource(R.string.title_pref_reset_sum),
                    icon = Icons.Default.RestartAlt,
                    onClick = onResetToDefaults
                )
            }
        }
    }
}

// ============================================
// CATEGORY CARD
// ============================================

@Composable
private fun SettingsCategoryCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
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
                .padding(SMTheme.spacing.spacing200)
        ) {
            // Category Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                SMIcon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = SMTheme.material.colorScheme.primary,
                    modifier = Modifier.size(SMTheme.size.size250)
                )

                Spacer(modifier = Modifier.size(SMTheme.spacing.spacing150))

                SMText(
                    text = title,
                    style = SMTheme.material.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SMTheme.material.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(SMTheme.spacing.spacing150))

            // Category Content
            content()
        }
    }
}

// ============================================
// SWITCH ITEM
// ============================================

@Composable
private fun SettingsSwitchItem(
    title: String,
    description: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = SMTheme.spacing.spacing100),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SMIcon(
            imageVector = icon,
            contentDescription = null,
            tint = SMTheme.material.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(SMTheme.size.size250)
        )

        Spacer(modifier = Modifier.size(SMTheme.spacing.spacing150))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            SMText(
                text = title,
                style = SMTheme.material.typography.bodyLarge,
                color = SMTheme.material.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(SMTheme.spacing.spacing50))

            SMText(
                text = description,
                style = SMTheme.material.typography.bodySmall,
                color = SMTheme.material.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.size(SMTheme.spacing.spacing150))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = SMTheme.material.colorScheme.primary,
                checkedTrackColor = SMTheme.material.colorScheme.primaryContainer,
                uncheckedThumbColor = SMTheme.material.colorScheme.outline,
                uncheckedTrackColor = SMTheme.material.colorScheme.surfaceVariant
            )
        )
    }
}

// ============================================
// ACTION ITEM
// ============================================

@Composable
private fun SettingsActionItem(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = SMTheme.spacing.spacing100),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SMIcon(
                imageVector = icon,
                contentDescription = null,
                tint = SMTheme.material.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(SMTheme.size.size250)
            )

            Spacer(modifier = Modifier.size(SMTheme.spacing.spacing150))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                SMText(
                    text = title,
                    style = SMTheme.material.typography.bodyLarge,
                    color = SMTheme.material.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(SMTheme.spacing.spacing50))

                SMText(
                    text = description,
                    style = SMTheme.material.typography.bodySmall,
                    color = SMTheme.material.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(SMTheme.spacing.spacing150))

        SMButton(
            text = title,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
