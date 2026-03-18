package com.signaturemaker.app.application.features.sign

import androidx.compose.runtime.Stable

/**
 * Sealed interface representing all possible actions in the Sign Screen
 * Follows State Hoisting and Unidirectional Data Flow patterns
 */
@Stable
sealed interface SignScreenAction {
    /**
     * Navigation actions
     */
    data object NavigateToGallery : SignScreenAction
}
