package com.signaturemaker.app.application.features.sign

import android.Manifest
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.analytics.Analytics
import com.signaturemaker.app.application.core.extensions.shareSign
import com.signaturemaker.app.application.ui.ads.rememberInterstitialAd
import com.signaturemaker.app.application.ui.designsystem.SMTheme
import com.signaturemaker.app.application.ui.designsystem.components.SMColorSelector
import com.signaturemaker.app.application.ui.designsystem.components.SMIconButton
import com.signaturemaker.app.application.ui.designsystem.components.SMImageSelector
import com.signaturemaker.app.application.ui.designsystem.components.SMLineSeparator
import com.signaturemaker.app.application.ui.designsystem.components.SMModalBottomSheet
import com.signaturemaker.app.application.ui.designsystem.components.SMText
import com.signaturemaker.app.application.ui.snackbar.LocalSnackbarController
import com.tuppersoft.signaturepad.compose.SignaturePad
import com.tuppersoft.signaturepad.compose.SignaturePadConfig
import com.tuppersoft.signaturepad.compose.SignaturePadState
import com.tuppersoft.signaturepad.compose.rememberSignaturePadState
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

// ============================================
// MAIN SCREEN
// ============================================

/**
 * Sign Screen - Main composable for signature creation
 *
 * Allows users to:
 * - Draw signatures on a canvas
 * - Customize signature appearance (color, stroke width, background)
 * - Save and share signatures
 *
 * @param modifier Modifier to be applied to the root element
 * @param viewModel ViewModel for handling business logic (injected via Koin)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignScreen(
    modifier: Modifier = Modifier,
    viewModel: SignBoardViewModel = koinViewModel(),
    onNavigate: (SignScreenAction) -> Unit = {}
) {
    // ============================================
    // ANALYTICS TRACKING
    // ============================================
    // Track screen view and session
    DisposableEffect(Unit) {
        Analytics.trackScreen("sign_screen")

        onDispose {
            Analytics.trackSignScreenSessionEnd()
        }
    }

    // ============================================
    // STATE & INITIALIZATION
    // ============================================
    val signState = rememberSignState(
        initialColor = SignScreenDefaults.defaultPenColor,
        initialImage = SignScreenDefaults.defaultBackgroundImage,
        initialMinStrokeWidth = SignScreenDefaults.defaultMinStrokeWidth,
        initialMaxStrokeWidth = SignScreenDefaults.defaultMaxStrokeWidth
    )

    val snackbarController = LocalSnackbarController.current
    val currentActivity = LocalActivity.current
    val context = LocalContext.current
    // Intersticial (full screen ad) - Se muestra después de guardar firma
    val interstitialManager = rememberInterstitialAd(
        adUnitId = stringResource(R.string.interstitial_ad_unit_id),
        preload = true
    )

    // Storage permission - varies by Android version
    val storagePermission = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> Manifest.permission.READ_MEDIA_IMAGES
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> null // No permission needed for MediaStore on Android 10+
        else -> Manifest.permission.WRITE_EXTERNAL_STORAGE
    }

    // Pending action to execute after permission is granted
    var pendingAction by remember { mutableStateOf<SignUiEvent?>(null) }
    val permissionErrorMessage = stringResource(R.string.message_error_permission_required)

    // Permission launcher for storage access (Android 8-9 only)
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            // Execute pending action
            pendingAction?.let { event ->
                handleSignUiEvent(event, viewModel)
            }
        } else {
            snackbarController?.showError(message = permissionErrorMessage)
        }
        pendingAction = null
    }

    // UI event handler with permission check
    val onSignUiEvent: (SignUiEvent) -> Unit = { event ->
        // Track analytics for save/share events
        when (event) {
            is SignUiEvent.SavePngTransparent,
            is SignUiEvent.SavePngWhiteBackground -> {
                Analytics.trackSignatureSaved(
                    isTransparent = event is SignUiEvent.SavePngTransparent,
                    penColor = signState.selectedColor.toArgb().toLong(),
                    strokeMin = signState.strokeWidthRange.start,
                    strokeMax = signState.strokeWidthRange.endInclusive,
                    backgroundType = signState.selectedImage
                )
            }

            is SignUiEvent.SharePngTransparent,
            is SignUiEvent.SharePngWhiteBackground -> {
                Analytics.trackSignatureShared(event is SignUiEvent.SharePngTransparent)
            }
        }

        // Check if we need to request permission (only for Android 8-9)
        if (storagePermission != null && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            // Request permission and store the pending action
            pendingAction = event
            permissionLauncher.launch(storagePermission)
        } else {
            // No permission needed (Android 10+) or already granted
            handleSignUiEvent(event, viewModel)
        }
    }

    // Observe ViewModel one-shot events via Channel flows
    val savedMessage = stringResource(R.string.message_file_saved_successfully)
    val errorMessage = stringResource(R.string.message_error_saving_file)

    LaunchedEffect(Unit) {
        viewModel.saveBitmap.collect { uriResponse ->
            currentActivity?.let { activity ->
                if (uriResponse.share) {
                    // COMPARTIR: mostrar intersticial, y cuando se cierre, abrir diálogo de compartir
                    interstitialManager.showIfReady(activity) {
                        Analytics.trackInterstitialShown()
                        activity.shareSign(uriResponse.uri)
                    }
                } else {
                    // GUARDAR: primero snackbar, esperar, luego intersticial
                    snackbarController?.showSuccess(message = savedMessage)
                    delay(2000) // Dar tiempo al usuario para ver el mensaje
                    interstitialManager.showIfReady(activity) {
                        Analytics.trackInterstitialShown()
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.failure.collect {
            snackbarController?.showError(message = errorMessage)
            Analytics.trackSignatureSaveFailed()
        }
    }

    SignScreenContent(
        signState = signState,
        onUiEvent = onSignUiEvent,
        onNavigate = onNavigate,
        modifier = modifier
    )
}

/**
 * Sign Screen Content - Layout and structure
 */
@Composable
private fun SignScreenContent(
    signState: SignUIState,
    onUiEvent: (SignUiEvent) -> Unit,
    onNavigate: (SignScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val padState = rememberSignaturePadState()
    signState.padState = padState

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(createTiledBackgroundBrush(signState.selectedImage))
    ) {
        // Signature canvas
        SignaturePadView(signState = signState, padState = padState)

        // Options bottom sheet
        SignatureOptionsBottomSheet(
            signState = signState,
            onUiEvent = onUiEvent,
            onNavigate = onNavigate
        )

        // "Sign here" placeholder
        SignHerePlaceholder(
            visible = signState.showSignHere,
            modifier = Modifier.align(Alignment.Center)
        )

        // Undo / Redo buttons
        UndoRedoButtons(
            canUndo = padState.canUndo(),
            canRedo = padState.canRedo(),
            onUndo = { padState.undo() },
            onRedo = { padState.redo() },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(SMTheme.spacing.spacing200)
        )

        // Floating Action Button for options
        OptionsFloatingButton(
            onShowOptions = { signState.showBottomSheet() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(SMTheme.spacing.spacing200)
        )
    }
}

// ============================================
// SIGNATURE PAD
// ============================================

/**
 * Signature Pad View - Compose SignaturePad from tuppersoft library
 */
@Composable
private fun SignaturePadView(signState: SignUIState, padState: SignaturePadState) {
    SignaturePad(
        state = padState,
        config = SignaturePadConfig(
            penColor = signState.selectedColor,
            penMinWidth = signState.strokeWidthRange.start.dp,
            penMaxWidth = signState.strokeWidthRange.endInclusive.dp,
            velocitySmoothness = SignaturePadVelocityFilterWeight
        ),
        onStartSign = {
            signState.updateShowSignHere(false)
        },
        onSign = {
            signState.updateShowSignHere(false)
        },
        onClear = {
            signState.updateShowSignHere(true)
        },
        modifier = Modifier.fillMaxSize()
    )
}

// ============================================
// UI COMPONENTS
// ============================================

/**
 * "Sign Here" Placeholder Text
 */
@Composable
private fun SignHerePlaceholder(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    if (visible) {
        SMText(
            text = stringResource(R.string.title_SingHere),
            modifier = modifier,
            color = SMTheme.color.backgroundTextSheet
        )
    }
}

/**
 * Undo / Redo buttons - bottom-left corner with visible styling
 */
@Composable
private fun UndoRedoButtons(
    canUndo: Boolean,
    canRedo: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = SMTheme.material.colorScheme.surface.copy(alpha = 0.85f),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            IconButton(onClick = onUndo, enabled = canUndo) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Undo,
                    contentDescription = stringResource(R.string.action_undo),
                    tint = if (canUndo) SMTheme.material.colorScheme.onSurface
                    else SMTheme.material.colorScheme.onSurface.copy(alpha = 0.3f),
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(onClick = onRedo, enabled = canRedo) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Redo,
                    contentDescription = stringResource(R.string.action_redo),
                    tint = if (canRedo) SMTheme.material.colorScheme.onSurface
                    else SMTheme.material.colorScheme.onSurface.copy(alpha = 0.3f),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

/**
 * Options Floating Action Button
 * Modern FAB following Material Design 3 guidelines
 * Located at bottom-right corner for easy thumb access
 */
@Composable
private fun OptionsFloatingButton(
    onShowOptions: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onShowOptions,
        modifier = modifier,
        shape = CircleShape,
        containerColor = SMTheme.material.colorScheme.primaryContainer,
        contentColor = SMTheme.material.colorScheme.onPrimaryContainer,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = SMTheme.spacing.spacing100,
            pressedElevation = SMTheme.spacing.spacing150
        )
    ) {
        Icon(
            imageVector = Icons.Default.Tune,
            contentDescription = stringResource(R.string.title_options),
            modifier = Modifier.size(SMTheme.size.size300)
        )
    }
}

// ============================================
// BOTTOM SHEET
// ============================================

/**
 * Signature Options Bottom Sheet
 * Contains all signature customization options
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignatureOptionsBottomSheet(
    signState: SignUIState,
    onUiEvent: (SignUiEvent) -> Unit,
    onNavigate: (SignScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    SMModalBottomSheet(
        onDismissRequest = { signState.closeBottomSheet() },
        sheetState = signState.sheetState,
        modifier = modifier,
        openBottomSheet = signState.isShowBottomSheet
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SMTheme.spacing.spacing200)
        ) {
            BottomSheetHeader()
            ActionsSection(
                signState = signState,
                onUiEvent = onUiEvent,
                onNavigate = onNavigate
            )
            Spacer(modifier = Modifier.height(SMTheme.spacing.spacing50))
            ColorSection(signState = signState)
            StrokeWidthSection(signState = signState)
            BackgroundSection(signState = signState)
        }
    }
}

/**
 * Bottom Sheet Header
 */
@Composable
private fun BottomSheetHeader() {
    SMText(
        text = stringResource(R.string.title_signature_options),
        style = SMTheme.material.typography.headlineSmall,
        color = SMTheme.material.colorScheme.onSurface,
        modifier = Modifier.padding(
            horizontal = SMTheme.spacing.spacing200,
            vertical = SMTheme.spacing.spacing150
        )
    )
}

/**
 * Actions Section - Clear, Save, Share, Gallery
 */
@Composable
private fun ActionsSection(
    signState: SignUIState,
    onUiEvent: (SignUiEvent) -> Unit,
    onNavigate: (SignScreenAction) -> Unit
) {
    BottomSheetSection(title = stringResource(R.string.title_actions)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SMTheme.spacing.spacing100),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ClearButton(signState = signState)
            SaveButtonWithDropdown(signState = signState, onUiEvent = onUiEvent)
            ShareButtonWithDropdown(signState = signState, onUiEvent = onUiEvent)
            GalleryButton(onNavigate = onNavigate)
        }
    }
}

/**
 * Clear Button
 */
@Composable
private fun ClearButton(signState: SignUIState) {
    SMIconButton(
        imageVector = Icons.Default.CleaningServices,
        label = stringResource(R.string.title_clean),
        color = SMTheme.material.colorScheme.onSurfaceVariant,
        onClick = {
            Analytics.trackSignatureCleared()
            signState.clearSignature()
            signState.closeBottomSheet()
        }
    )
}

/**
 * Save Button with Dropdown Menu
 */
@Composable
private fun SaveButtonWithDropdown(
    signState: SignUIState,
    onUiEvent: (SignUiEvent) -> Unit
) {
    Box {
        SMIconButton(
            imageVector = Icons.Default.Save,
            label = stringResource(R.string.title_bSave),
            color = SMTheme.material.colorScheme.onSurfaceVariant,
            onClick = { signState.showSaveDropdown() }
        )

        DropdownMenu(
            expanded = signState.showSaveDropdown,
            onDismissRequest = { signState.closeSaveDropdown() }
        ) {
            DropdownMenuItem(
                text = { SMText(text = stringResource(R.string.title_save_png)) },
                onClick = {
                    onUiEvent(
                        SignUiEvent.SavePngTransparent(
                            bitmap = signState.getTransparentSignatureBitmap()
                        )
                    )
                    signState.closeSaveDropdown()
                    signState.closeBottomSheet()
                }
            )
            DropdownMenuItem(
                text = { SMText(text = stringResource(R.string.title_save_png_wh)) },
                onClick = {
                    onUiEvent(
                        SignUiEvent.SavePngWhiteBackground(
                            bitmap = signState.getWhiteBackgroundSignatureBitmap()
                        )
                    )
                    signState.closeSaveDropdown()
                    signState.closeBottomSheet()
                }
            )
        }
    }
}

/**
 * Share Button with Dropdown Menu
 */
@Composable
private fun ShareButtonWithDropdown(
    signState: SignUIState,
    onUiEvent: (SignUiEvent) -> Unit
) {
    Box {
        SMIconButton(
            imageVector = Icons.Default.Share,
            label = stringResource(R.string.share),
            color = SMTheme.material.colorScheme.onSurfaceVariant,
            onClick = { signState.showShareDropdown() }
        )

        DropdownMenu(
            expanded = signState.showShareDropdown,
            onDismissRequest = { signState.closeShareDropdown() }
        ) {
            DropdownMenuItem(
                text = { SMText(text = stringResource(R.string.title_save_png)) },
                onClick = {
                    onUiEvent(
                        SignUiEvent.SharePngTransparent(
                            bitmap = signState.getTransparentSignatureBitmap()
                        )
                    )
                    signState.closeShareDropdown()
                    signState.closeBottomSheet()
                }
            )
            DropdownMenuItem(
                text = { SMText(text = stringResource(R.string.title_save_png_wh)) },
                onClick = {
                    onUiEvent(
                        SignUiEvent.SharePngWhiteBackground(
                            bitmap = signState.getWhiteBackgroundSignatureBitmap()
                        )
                    )
                    signState.closeShareDropdown()
                    signState.closeBottomSheet()
                }
            )
        }
    }
}

/**
 * Gallery Button - Navigate to saved signatures
 */
@Composable
private fun GalleryButton(onNavigate: (SignScreenAction) -> Unit) {
    SMIconButton(
        imageVector = Icons.Default.Collections,
        label = stringResource(R.string.title_gallery),
        color = SMTheme.material.colorScheme.onSurfaceVariant,
        onClick = {
            onNavigate(SignScreenAction.NavigateToGallery)
        }
    )
}

/**
 * Color Section - Pen color selection
 */
@Composable
private fun ColorSection(signState: SignUIState) {
    BottomSheetSection(title = stringResource(R.string.title_bColor)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SMTheme.spacing.spacing100),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SignScreenDefaults.availablePenColors().forEach { color ->
                SMColorSelector(
                    color = color,
                    selected = color == signState.selectedColor,
                    modifier = Modifier.size(SMTheme.size.size500),
                    onClick = {
                        Analytics.trackPenColorChanged(color.toArgb().toLong())
                        signState.updateColor(color)
                    }
                )
            }
        }
    }
}

/**
 * Stroke Width Section - Pen thickness control
 */
@Composable
private fun StrokeWidthSection(signState: SignUIState) {
    BottomSheetSection(title = stringResource(R.string.title_bStroke)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RangeSlider(
                value = signState.strokeWidthRange,
                onValueChange = { signState.updateStrokeWidth(it) },
                onValueChangeFinished = {
                    Analytics.trackStrokeWidthChanged(
                        minWidth = signState.strokeWidthRange.start,
                        maxWidth = signState.strokeWidthRange.endInclusive
                    )
                },
                valueRange = StrokeWidthValueRange,
                steps = StrokeWidthSliderSteps,
                modifier = Modifier.padding(horizontal = SMTheme.spacing.spacing100)
            )

            Spacer(modifier = Modifier.height(SMTheme.spacing.spacing50))

            SMText(
                text = buildStrokeWidthLabel(
                    min = signState.strokeWidthRange.start,
                    max = signState.strokeWidthRange.endInclusive
                ),
                style = SMTheme.material.typography.bodyMedium,
                color = SMTheme.material.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Background Section - Background pattern selection (color or images)
 */
@Composable
private fun BackgroundSection(signState: SignUIState) {
    BottomSheetSection(title = stringResource(R.string.title_background)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SMTheme.spacing.spacing100),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BackgroundManager.getAvailableOptions().forEach { optionId ->
                when (val backgroundType = BackgroundType.fromId(optionId)) {
                    is BackgroundType.ThemeColor -> {
                        SMColorSelector(
                            color = SMTheme.color.backgroundSheet,
                            selected = optionId == signState.selectedImage,
                            modifier = Modifier.size(SMTheme.size.size500),
                            onClick = {
                                Analytics.trackBackgroundChanged(optionId)
                                signState.updateImage(optionId)
                            }
                        )
                    }

                    is BackgroundType.Image -> {
                        SMImageSelector(
                            image = painterResource(id = backgroundType.resourceId),
                            selected = optionId == signState.selectedImage,
                            modifier = Modifier.size(SMTheme.size.size500),
                            onClick = {
                                Analytics.trackBackgroundChanged(optionId)
                                signState.updateImage(optionId)
                            }
                        )
                    }
                }
            }
        }
    }
}

// ============================================
// REUSABLE COMPONENTS
// ============================================

/**
 * Bottom Sheet Section - Reusable section container
 */
@Composable
private fun BottomSheetSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = SMTheme.spacing.spacing200,
                vertical = SMTheme.spacing.spacing100
            )
    ) {
        SMText(
            text = title,
            style = SMTheme.material.typography.titleMedium,
            color = SMTheme.material.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(SMTheme.spacing.spacing150))

        content()

        Spacer(modifier = Modifier.height(SMTheme.spacing.spacing100))

        SMLineSeparator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = SMTheme.spacing.spacing150)
        )
    }
}

// ============================================
// HELPER FUNCTIONS
// ============================================

/**
 * Create background brush using BackgroundManager
 */
@Composable
private fun createTiledBackgroundBrush(@DrawableRes imageId: Int): Brush {
    val backgroundType = BackgroundType.fromId(imageId)
    return BackgroundManager.createBrush(backgroundType)
}

/**
 * Build stroke width label with min and max values
 */
@Composable
private fun buildStrokeWidthLabel(min: Float, max: Float): String {
    return "${
        stringResource(
            R.string.min
        )
    } ${"%.1f".format(min)} • ${stringResource(R.string.max)} ${"%.1f".format(max)}"
}

private val StrokeWidthValueRange = 1f..10f
private const val StrokeWidthSliderSteps = 0
private const val SignaturePadVelocityFilterWeight = 0.1f
