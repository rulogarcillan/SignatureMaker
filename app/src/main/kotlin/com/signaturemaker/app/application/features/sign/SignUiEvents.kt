package com.signaturemaker.app.application.features.sign

import android.graphics.Bitmap
import androidx.compose.runtime.Stable
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
    abstract val bitmap: Bitmap?
    abstract val displayName: String?
    abstract val isShare: Boolean

    /**
     * Save signature as PNG with transparent background
     */
    data class SavePngTransparent(override val bitmap: Bitmap?, override val displayName: String? = null) : SignUiEvent() {
        override val isShare: Boolean get() = false
    }

    /**
     * Save signature as PNG with white background
     */
    data class SavePngWhiteBackground(override val bitmap: Bitmap?, override val displayName: String? = null) : SignUiEvent() {
        override val isShare: Boolean get() = false
    }

    /**
     * Share signature as PNG with transparent background
     */
    data class SharePngTransparent(override val bitmap: Bitmap?, override val displayName: String? = null) : SignUiEvent() {
        override val isShare: Boolean get() = true
    }

    /**
     * Share signature as PNG with white background
     */
    data class SharePngWhiteBackground(override val bitmap: Bitmap?, override val displayName: String? = null) : SignUiEvent() {
        override val isShare: Boolean get() = true
    }
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
    viewModel.saveFileBitmap(
        share = event.isShare,
        bitmap = event.bitmap,
        pathToSave = pathToSave,
        displayName = event.displayName
    )
}
