package com.signaturemaker.app.application.features.sign

import android.content.Context
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
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.tuppersoft.signaturepad.compose.SignaturePadState
import com.signaturemaker.app.application.core.extensions.Utils
import com.tuppersoft.skizo.android.core.extension.loadSharedPreference
import com.tuppersoft.skizo.android.core.extension.saveSharedPreference
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
    private val appContext: Context,
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

    // Reference to the new SignaturePadState from tuppersoft library
    var padState: SignaturePadState? = null

    /**
     * Get signature bitmap with transparent background
     */
    fun getTransparentSignatureBitmap(): Bitmap? {
        return padState?.toTransparentBitmap(crop = true, paddingCrop = 16)?.asAndroidBitmap()
    }

    /**
     * Get signature bitmap with white background
     */
    fun getWhiteBackgroundSignatureBitmap(): Bitmap? {
        return padState?.toBitmap(crop = true, paddingCrop = 16)?.asAndroidBitmap()
    }

    /**
     * Check if signature pad is empty
     */
    fun isSignatureEmpty(): Boolean {
        return padState?.isEmpty ?: true
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

        // Si el usuario tiene activada la opción de recordar color, guardarlo
        if (appContext.loadSharedPreference(com.signaturemaker.app.application.utils.Constants.ID_PREF_COLOR, false)) {
            Utils.penColor = color.toArgb()
            appContext.saveSharedPreference(
                com.signaturemaker.app.application.utils.Constants.PREF_COLOR,
                Utils.penColor
            )
        }
    }

    fun updateImage(@DrawableRes image: Int) {
        selectedImage = image

        // Si el usuario tiene activada la opción de recordar wallpaper, guardarlo
        if (appContext.loadSharedPreference(
                com.signaturemaker.app.application.utils.Constants.ID_PREF_WALLPAPER,
                false
            )
        ) {
            Utils.wallpaper = image
            appContext.saveSharedPreference(
                com.signaturemaker.app.application.utils.Constants.PREF_WALLPAPER,
                Utils.wallpaper
            )
        }
    }

    fun updateStrokeWidth(range: ClosedFloatingPointRange<Float>) {
        strokeWidthRange = range

        // Si el usuario tiene activada la opción de recordar stroke, guardarlo
        if (appContext.loadSharedPreference(com.signaturemaker.app.application.utils.Constants.ID_PREF_STROKE, false)) {
            Utils.minStroke = range.start
            Utils.maxStroke = range.endInclusive
            appContext.saveSharedPreference(
                com.signaturemaker.app.application.utils.Constants.PREF_MIN_STROKE,
                Utils.minStroke
            )
            appContext.saveSharedPreference(
                com.signaturemaker.app.application.utils.Constants.PREF_MAX_STROKE,
                Utils.maxStroke
            )
        }
    }

    fun clearSignature() {
        padState?.clear()
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
    appContext: Context = LocalContext.current,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true }
    ),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): SignUIState {
    return remember(sheetState, coroutineScope, appContext) {
        SignUIState(
            sheetState = sheetState,
            coroutineScope = coroutineScope,
            appContext = appContext,
            initialColor = initialColor,
            initialImage = initialImage,
            initialMinStrokeWidth = initialMinStrokeWidth,
            initialMaxStrokeWidth = initialMaxStrokeWidth
        )
    }
}
