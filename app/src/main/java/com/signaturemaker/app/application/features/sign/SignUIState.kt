package com.signaturemaker.app.application.features.sign

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/*
 * State holder for Sign Screen following Compose best practices
 * All state is properly hoisted and observable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Stable
class SignUIState(
    val sheetState: SheetState,
    private val coroutineScope: CoroutineScope,
    initialColor: Color,
    @DrawableRes initialImage: Int,
    initialMinStrokeWidth: Float,
    initialMaxStrokeWidth: Float
) {
    // Observable state properties
    var showSignHere by mutableStateOf(true)
        private set

    var isShowBottomSheet by mutableStateOf(false)
        private set

    var showSaveDropdown by mutableStateOf(false)
        private set

    var showShareDropdown by mutableStateOf(false)
        private set

    var selectedColor by mutableStateOf(initialColor)
        private set

    var selectedImage by mutableIntStateOf(initialImage)
        private set

    var strokeWidthRange by mutableStateOf(initialMinStrokeWidth..initialMaxStrokeWidth)
        private set

    // Reference to clear function from SignaturePad
    var clearFunction: (() -> Unit)? = null

    // Reference to SignaturePad for getting bitmap
    var signaturePadRef: SignaturePad? = null

    /**
     * Get signature bitmap with transparent background
     */
    fun getTransparentSignatureBitmap(): Bitmap? {
        return signaturePadRef?.getTransparentSignatureBitmap(true)
    }

    /**
     * Get signature bitmap with white background
     */
    fun getWhiteBackgroundSignatureBitmap(): Bitmap? {
        return signaturePadRef?.signatureBitmap
    }

    /**
     * Check if signature pad is empty
     */
    fun isSignatureEmpty(): Boolean {
        return signaturePadRef?.isEmpty ?: true
    }

    // State mutation functions
    fun updateShowSignHere(show: Boolean) {
        showSignHere = show
    }

    fun showBottomSheet() {
        isShowBottomSheet = true
    }

    fun closeBottomSheet() {
        coroutineScope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                isShowBottomSheet = false
            }
        }
    }

    fun showSaveDropdown() {
        showSaveDropdown = true
    }

    fun closeSaveDropdown() {
        showSaveDropdown = false
    }

    fun showShareDropdown() {
        showShareDropdown = true
    }

    fun closeShareDropdown() {
        showShareDropdown = false
    }

    fun updateColor(color: Color) {
        selectedColor = color
    }

    fun updateImage(@DrawableRes image: Int) {
        selectedImage = image
    }

    fun updateStrokeWidth(range: ClosedFloatingPointRange<Float>) {
        strokeWidthRange = range
    }

    fun clearSignature() {
        clearFunction?.invoke()
    }
}

/*
 * Remember SignState following Compose best practices
 * The state holder is remembered and its internal state is properly observable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberSignState(
    initialColor: Color,
    @DrawableRes initialImage: Int,
    initialMinStrokeWidth: Float = 2f,
    initialMaxStrokeWidth: Float = 5f,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true }
    ),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): SignUIState {
    return remember(sheetState, coroutineScope) {
        SignUIState(
            sheetState = sheetState,
            coroutineScope = coroutineScope,
            initialColor = initialColor,
            initialImage = initialImage,
            initialMinStrokeWidth = initialMinStrokeWidth,
            initialMaxStrokeWidth = initialMaxStrokeWidth
        )
    }
}
