package com.signaturemaker.app.application.features.sign

import android.view.LayoutInflater
import androidx.activity.compose.LocalActivity
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
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.github.gcacace.signaturepad.views.SignaturePad
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.extensions.shareSign
import com.signaturemaker.app.application.ui.designsystem.SMTheme
import com.signaturemaker.app.application.ui.designsystem.components.SMColorSelector
import com.signaturemaker.app.application.ui.designsystem.components.SMIconButton
import com.signaturemaker.app.application.ui.designsystem.components.SMImageSelector
import com.signaturemaker.app.application.ui.designsystem.components.SMLineSeparator
import com.signaturemaker.app.application.ui.designsystem.components.SMModalBottomSheet
import com.signaturemaker.app.application.ui.designsystem.components.SMText
import com.signaturemaker.app.application.ui.snackbar.LocalSnackbarController
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
    viewModel: SignBoardViewModel = koinViewModel()
) {
    val signState = rememberSignState(
        initialColor = SignScreenDefaults.defaultPenColor,
        initialImage = SignScreenDefaults.defaultBackgroundImage
    )

    val snackbarController = LocalSnackbarController.current
    val currentActivity = LocalActivity.current

    // UI event handler with state hoisting pattern
    val onSignUiEvent: (SignUiEvent) -> Unit = { event ->
        handleSignUiEvent(event, viewModel)
    }

    // Observe ViewModel events
    val saveBitmapEvent by viewModel.saveBitmap.observeAsState()
    val failureEvent by viewModel.failure.observeAsState()

    // Handle save success
    saveBitmapEvent?.getContentIfNotHandled()?.let { uriResponse ->
        if (uriResponse.share) {
            currentActivity?.shareSign(uriResponse.uri)
        } else {
            snackbarController?.showSuccess(
                message = stringResource(R.string.message_file_saved_successfully)
            )
        }
    }

    // Handle save failure
    failureEvent?.getContentIfNotHandled()?.let {
        snackbarController?.showError(
            message = stringResource(R.string.message_error_saving_file)
        )
    }

    SignScreenContent(
        signState = signState,
        onUiEvent = onSignUiEvent,
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
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(createTiledBackgroundBrush(signState.selectedImage))
    ) {
        // Signature canvas
        SignaturePadView(signState = signState)

        // Options bottom sheet
        SignatureOptionsBottomSheet(
            signState = signState,
            onUiEvent = onUiEvent
        )

        // "Sign here" placeholder
        SignHerePlaceholder(
            visible = signState.showSignHere,
            modifier = Modifier.align(Alignment.Center)
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
 * Signature Pad View - AndroidView wrapper for legacy XML SignaturePad
 */
@Composable
private fun SignaturePadView(signState: SignUIState) {
    AndroidView(
        factory = { context ->
            LayoutInflater.from(context).inflate(R.layout.signature_pad, null, false)
        },
        update = { view ->
            (view as SignaturePad).apply {
                // Store reference for bitmap extraction
                signState.signaturePadRef = this

                // Configure pen properties
                setPenColor(signState.selectedColor.toArgb())
                setMinWidth(signState.strokeWidthRange.start)
                setMaxWidth(signState.strokeWidthRange.endInclusive)
                setVelocityFilterWeight(SignaturePadVelocityFilterWeight)

                // Store clear function
                signState.clearFunction = { clear() }

                // Configure listeners
                setOnSignedListener(createSignaturePadListener(signState))
            }
        }
    )
}

/**
 * Create SignaturePad listener for state updates
 */
private fun createSignaturePadListener(signState: SignUIState) = object : SignaturePad.OnSignedListener {
    override fun onStartSigning() {
        signState.updateShowSignHere(false)
    }

    override fun onClear() {
        signState.updateShowSignHere(true)
    }

    override fun onSigned() {
        signState.updateShowSignHere(false)
    }
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
            modifier = modifier
        )
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
            ActionsSection(signState = signState, onUiEvent = onUiEvent)
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
 * Actions Section - Clear, Save, Share
 */
@Composable
private fun ActionsSection(
    signState: SignUIState,
    onUiEvent: (SignUiEvent) -> Unit
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
        label = stringResource(R.string.tittle_clean),
        color = SMTheme.material.colorScheme.onSurfaceVariant,
        onClick = {
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
                    onClick = { signState.updateColor(color) }
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
                onValueChangeFinished = {},
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
 * Background Section - Background pattern selection
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
            SignScreenDefaults.availableBackgroundImages().forEach { imageRes ->
                SMImageSelector(
                    image = painterResource(id = imageRes),
                    selected = imageRes == signState.selectedImage,
                    modifier = Modifier.size(SMTheme.size.size500),
                    onClick = { signState.updateImage(imageRes) }
                )
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
 * Create a tiled background brush from an image resource
 */
@Composable
private fun createTiledBackgroundBrush(@DrawableRes imageRes: Int): Brush {
    val imageBitmap = ImageBitmap.imageResource(LocalResources.current, imageRes)
    val shader = ImageShader(imageBitmap, TileMode.Repeated, TileMode.Repeated)
    return ShaderBrush(shader)
}

/**
 * Build stroke width label with min and max values
 */
@Composable
private fun buildStrokeWidthLabel(min: Float, max: Float): String {
    return "${stringResource(R.string.min)} ${"%.1f".format(min)} • ${stringResource(R.string.max)} ${"%.1f".format(max)}"
}


object SignScreenDefaults {

    val defaultPenColor: Color
        @Composable
        get() = SMTheme.color.pen1

    val defaultBackgroundImage: Int
        get() = R.drawable.mascara3

    @Composable
    fun availablePenColors() = listOf(
        SMTheme.color.pen1,
        SMTheme.color.pen2,
        SMTheme.color.pen3,
        SMTheme.color.pen4
    )

    fun availableBackgroundImages() = listOf(
        R.drawable.mascara3,
        R.drawable.mascara1,
        R.drawable.mascara2
    )
}


private val StrokeWidthValueRange = 1f..10f
private const val StrokeWidthSliderSteps = 0
private const val SignaturePadVelocityFilterWeight = 0.1f
