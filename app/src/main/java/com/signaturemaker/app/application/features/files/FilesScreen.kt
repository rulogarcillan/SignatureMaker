package com.signaturemaker.app.application.features.files

import android.Manifest
import android.app.Activity
import android.net.Uri
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.application.core.extensions.shareSign
import com.signaturemaker.app.application.ui.designsystem.SMTheme
import com.signaturemaker.app.application.ui.designsystem.components.SMText
import com.signaturemaker.app.application.ui.snackbar.LocalSnackbarController
import com.signaturemaker.app.domain.models.ItemFile

// ============================================
// MAIN SCREEN
// ============================================

/**
 * Files Screen - Main composable for viewing saved signatures
 *
 * Displays a list of all saved signature files with options to:
 * - View signature details
 * - Share signatures
 * - Delete signatures with undo functionality
 *
 * @param modifier Modifier to be applied to the root element
 * @param viewModel ViewModel for handling business logic
 */
@Composable
fun FilesScreen(
    modifier: Modifier = Modifier,
    viewModel: FilesViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarController = LocalSnackbarController.current
    val currentActivity = LocalActivity.current
    val context = LocalContext.current

    // Permission launcher for storage access
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    }

    var hasPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (granted) {
            viewModel.loadFiles(Utils.path)
        } else {
            snackbarController?.showError(
                message = context.getString(R.string.message_error_permission_required)
            )
        }
    }

    // IntentSender launcher for delete permission on Android 10+
    val deletePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // User granted permission, retry delete
            viewModel.retryDelete()
            // Snackbar se mostrará automáticamente por deleteSuccess
        } else {
            // User denied permission - restore the file in the list
            viewModel.clearPendingDeleteIntent()
            // Reload to restore the file
            viewModel.loadFiles(Utils.path)
        }
    }

    // Handle pending delete intent
    LaunchedEffect(uiState.pendingDeleteIntent) {
        uiState.pendingDeleteIntent?.let { pendingIntent ->
            try {
                deletePermissionLauncher.launch(
                    androidx.activity.result.IntentSenderRequest.Builder(pendingIntent.intentSender).build()
                )
            } catch (e: Exception) {
                viewModel.clearPendingDeleteIntent()
                snackbarController?.showError(
                    message = "Error: ${e.message}"
                )
            }
        }
    }

    // Load files on first composition
    LaunchedEffect(Unit) {
        permissionLauncher.launch(permission)
    }

    // Handle errors
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarController?.showError(
                message = context.getString(R.string.message_error_loading_files)
            )
            viewModel.clearError()
        }
    }

    // Handle delete success
    LaunchedEffect(uiState.deleteSuccess) {
        if (uiState.deleteSuccess) {
            android.util.Log.d("FilesScreen", "Delete success detected, showing snackbar")
            snackbarController?.showSuccess(
                message = context.getString(R.string.message_file_deleted)
            ) ?: android.util.Log.e("FilesScreen", "SnackbarController is null!")
            viewModel.clearDeleteSuccess()
        }
    }

    FilesScreenContent(
        uiState = uiState,
        onFileClick = { file ->
            // TODO: Navigate to detail view
        },
        onShareClick = { file ->
            currentActivity?.shareSign(file.uri)
        },
        onDeleteClick = { file ->
            viewModel.onFileDismissed(file)
        },
        modifier = modifier
    )
}

/**
 * Files Screen Content - Layout and structure
 */
@Composable
private fun FilesScreenContent(
    uiState: FilesUiState,
    onFileClick: (ItemFile) -> Unit,
    onShareClick: (ItemFile) -> Unit,
    onDeleteClick: (ItemFile) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SMTheme.material.colorScheme.background)
    ) {
        when {
            uiState.isLoading -> LoadingState()
            uiState.files.isEmpty() -> EmptyState()
            else -> FilesList(
                files = uiState.files,
                onFileClick = onFileClick,
                onShareClick = onShareClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

// ============================================
// LOADING STATE
// ============================================

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = SMTheme.material.colorScheme.primary
        )
    }
}

// ============================================
// EMPTY STATE
// ============================================

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SMTheme.spacing.spacing300),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Description,
            contentDescription = null,
            modifier = Modifier.size(SMTheme.size.size700),
            tint = SMTheme.material.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(SMTheme.spacing.spacing200))

        SMText(
            text = stringResource(R.string.message_no_files),
            style = SMTheme.material.typography.titleLarge,
            color = SMTheme.material.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(SMTheme.spacing.spacing100))

        SMText(
            text = stringResource(R.string.message_no_files_description),
            style = SMTheme.material.typography.bodyMedium,
            color = SMTheme.material.colorScheme.onSurfaceVariant
        )
    }
}

// ============================================
// FILES LIST
// ============================================

@Composable
private fun FilesList(
    files: List<ItemFile>,
    onFileClick: (ItemFile) -> Unit,
    onShareClick: (ItemFile) -> Unit,
    onDeleteClick: (ItemFile) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = SMTheme.size.size1088),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(SMTheme.spacing.spacing200),
        horizontalArrangement = Arrangement.spacedBy(SMTheme.spacing.spacing150),
        verticalArrangement = Arrangement.spacedBy(SMTheme.spacing.spacing150)
    ) {
        items(
            items = files,
            key = { it.uri.toString() }
        ) { file ->
            FileItem(
                file = file,
                onFileClick = onFileClick,
                onShareClick = onShareClick,
                onDeleteClick = onDeleteClick,
                modifier = Modifier.animateItem()
            )
        }
    }
}

// ============================================
// FILE ITEM
// ============================================

@Composable
private fun FileItem(
    file: ItemFile,
    onFileClick: (ItemFile) -> Unit,
    onShareClick: (ItemFile) -> Unit,
    onDeleteClick: (ItemFile) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = { onFileClick(file) },
        shape = SMTheme.radius.roundedCornerShape250,
        colors = CardDefaults.cardColors(
            containerColor = SMTheme.material.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = SMTheme.size.size00,
            pressedElevation = SMTheme.size.size20
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image preview section
            FileThumbnail(
                uri = file.uri,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )

            // Info section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SMTheme.spacing.spacing150)
            ) {
                // File name
                SMText(
                    text = file.name.removePrefix("SM_").removeSuffix(".png").removeSuffix(".jpg").removeSuffix(".svg"),
                    style = SMTheme.material.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = SMTheme.material.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(SMTheme.spacing.spacing100))

                // Metadata
                Row(
                    horizontalArrangement = Arrangement.spacedBy(SMTheme.spacing.spacing50),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(SMTheme.size.size150),
                        tint = SMTheme.material.colorScheme.onSurfaceVariant
                    )
                    SMText(
                        text = file.date,
                        style = SMTheme.material.typography.bodySmall,
                        color = SMTheme.material.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(SMTheme.spacing.spacing50))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(SMTheme.spacing.spacing50),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FolderOpen,
                        contentDescription = null,
                        modifier = Modifier.size(SMTheme.size.size150),
                        tint = SMTheme.material.colorScheme.onSurfaceVariant
                    )
                    SMText(
                        text = file.size,
                        style = SMTheme.material.typography.bodySmall,
                        color = SMTheme.material.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(SMTheme.spacing.spacing150))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(SMTheme.spacing.spacing100)
                ) {
                    FilledTonalIconButton(
                        onClick = { onShareClick(file) },
                        modifier = Modifier
                            .weight(1f)
                            .size(SMTheme.size.size375),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = SMTheme.material.colorScheme.primaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.share),
                            modifier = Modifier.size(SMTheme.size.size200)
                        )
                    }

                    FilledTonalIconButton(
                        onClick = { onDeleteClick(file) },
                        modifier = Modifier
                            .weight(1f)
                            .size(SMTheme.size.size375),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = SMTheme.material.colorScheme.errorContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete),
                            modifier = Modifier.size(SMTheme.size.size200),
                            tint = SMTheme.material.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}

// ============================================
// FILE THUMBNAIL
// ============================================

@Composable
private fun FileThumbnail(
    uri: Uri,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .clip(SMTheme.radius.roundedCornerShape00)
            .background(SMTheme.material.colorScheme.surfaceContainerHighest),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(uri)
                .crossfade(true)
                .build(),
            contentDescription = "Signature preview",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}

