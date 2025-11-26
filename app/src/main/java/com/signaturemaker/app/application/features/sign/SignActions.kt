package com.signaturemaker.app.application.features.sign

import android.graphics.Bitmap
import androidx.compose.runtime.Stable
import com.signaturemaker.app.application.features.sign.SignAction.SavePngTransparent
import com.signaturemaker.app.application.features.sign.SignAction.SavePngWhiteBackground
import com.signaturemaker.app.application.features.sign.SignAction.SharePngTransparent
import com.signaturemaker.app.application.features.sign.SignAction.SharePngWhiteBackground
import com.signaturemaker.app.application.core.extensions.Utils

/**
 * Sealed class representing all possible actions in the Sign Screen
 * Follows State Hoisting and Unidirectional Data Flow patterns
 */
@Stable
sealed class SignAction {
    /**
     * Save signature as PNG with transparent background
     */
    data class SavePngTransparent(val bitmap: Bitmap?, val displayName: String? = null) : SignAction()

    /**
     * Save signature as PNG with white background
     */
    data class SavePngWhiteBackground(val bitmap: Bitmap?, val displayName: String? = null) : SignAction()

    /**
     * Share signature as PNG with transparent background
     */
    data class SharePngTransparent(val bitmap: Bitmap?, val displayName: String? = null) : SignAction()

    /**
     * Share signature as PNG with white background
     */
    data class SharePngWhiteBackground(val bitmap: Bitmap?, val displayName: String? = null) : SignAction()
}

/**
 * Handle SignAction events and communicate with ViewModel
 * Follows unidirectional data flow pattern
 *
 * @param action The action to handle
 * @param viewModel The ViewModel to interact with
 * @param pathToSave The path where files should be saved
 */
fun handleSignAction(
    action: SignAction,
    viewModel: SignBoardViewModel,
    pathToSave: String = Utils.path
) {
    when (action) {
        is SavePngTransparent -> {
            viewModel.saveFileBitmap(
                share = false,
                bitmap = action.bitmap,
                pathToSave = pathToSave,
                displayName = action.displayName
            )
        }
        is SavePngWhiteBackground -> {
            viewModel.saveFileBitmap(
                share = false,
                bitmap = action.bitmap,
                pathToSave = pathToSave,
                displayName = action.displayName
            )
        }
        is SharePngTransparent -> {
            viewModel.saveFileBitmap(
                share = true,
                bitmap = action.bitmap,
                pathToSave = pathToSave,
                displayName = action.displayName
            )
        }
        is SharePngWhiteBackground -> {
            viewModel.saveFileBitmap(
                share = true,
                bitmap = action.bitmap,
                pathToSave = pathToSave,
                displayName = action.displayName
            )
        }
    }
}
