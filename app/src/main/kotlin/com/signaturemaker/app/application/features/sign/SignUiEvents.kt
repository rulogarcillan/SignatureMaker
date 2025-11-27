package com.signaturemaker.app.application.features.sign

import android.graphics.Bitmap
import androidx.compose.runtime.Stable
import com.signaturemaker.app.application.features.sign.SignUiEvent.SavePngTransparent
import com.signaturemaker.app.application.features.sign.SignUiEvent.SavePngWhiteBackground
import com.signaturemaker.app.application.features.sign.SignUiEvent.SharePngTransparent
import com.signaturemaker.app.application.features.sign.SignUiEvent.SharePngWhiteBackground
import com.signaturemaker.app.application.core.extensions.Utils

/**
 * UI Events for Sign Screen
 * Represents business logic actions that affect the ViewModel state
 * (e.g., save, share, delete, etc.)
 *
 * Follows State Hoisting and Unidirectional Data Flow patterns
 */
@Stable
sealed class SignUiEvent {
    /**
     * Save signature as PNG with transparent background
     */
    data class SavePngTransparent(val bitmap: Bitmap?, val displayName: String? = null) : SignUiEvent()

    /**
     * Save signature as PNG with white background
     */
    data class SavePngWhiteBackground(val bitmap: Bitmap?, val displayName: String? = null) : SignUiEvent()

    /**
     * Share signature as PNG with transparent background
     */
    data class SharePngTransparent(val bitmap: Bitmap?, val displayName: String? = null) : SignUiEvent()

    /**
     * Share signature as PNG with white background
     */
    data class SharePngWhiteBackground(val bitmap: Bitmap?, val displayName: String? = null) : SignUiEvent()
}

/**
 * Handle UI events and communicate with ViewModel
 * Follows unidirectional data flow pattern
 *
 * @param event The UI event to handle
 * @param viewModel The ViewModel to interact with
 * @param pathToSave The path where files should be saved
 */
fun handleSignUiEvent(
    event: SignUiEvent,
    viewModel: SignBoardViewModel,
    pathToSave: String = Utils.path
) {
    when (event) {
        is SavePngTransparent -> {
            viewModel.saveFileBitmap(
                share = false,
                bitmap = event.bitmap,
                pathToSave = pathToSave,
                displayName = event.displayName
            )
        }
        is SavePngWhiteBackground -> {
            viewModel.saveFileBitmap(
                share = false,
                bitmap = event.bitmap,
                pathToSave = pathToSave,
                displayName = event.displayName
            )
        }
        is SharePngTransparent -> {
            viewModel.saveFileBitmap(
                share = true,
                bitmap = event.bitmap,
                pathToSave = pathToSave,
                displayName = event.displayName
            )
        }
        is SharePngWhiteBackground -> {
            viewModel.saveFileBitmap(
                share = true,
                bitmap = event.bitmap,
                pathToSave = pathToSave,
                displayName = event.displayName
            )
        }
    }
}
