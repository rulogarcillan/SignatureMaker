package com.signaturemaker.app.application.ui.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.signaturemaker.app.application.ui.snackbar.SnackbarMessage.Error
import com.signaturemaker.app.application.ui.snackbar.SnackbarMessage.Info
import com.signaturemaker.app.application.ui.snackbar.SnackbarMessage.Success
import com.signaturemaker.app.application.ui.snackbar.SnackbarMessage.Warning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * CompositionLocal for accessing SnackbarController throughout the app
 */
val LocalSnackbarController = compositionLocalOf<SnackbarController?> { null }

/**
 * Sealed class representing different types of Snackbar messages
 * Follows State Hoisting pattern for scalability
 */
@Stable
sealed class SnackbarMessage {
    abstract val message: String
    abstract val actionLabel: String?
    abstract val duration: SnackbarDuration

    /**
     * Success message (e.g., file saved successfully)
     */
    data class Success(
        override val message: String,
        override val actionLabel: String? = null,
        override val duration: SnackbarDuration = SnackbarDuration.Short
    ) : SnackbarMessage()

    /**
     * Error message (e.g., failed to save file)
     */
    data class Error(
        override val message: String,
        override val actionLabel: String? = null,
        override val duration: SnackbarDuration = SnackbarDuration.Long
    ) : SnackbarMessage()

    /**
     * Info message (e.g., general information)
     */
    data class Info(
        override val message: String,
        override val actionLabel: String? = null,
        override val duration: SnackbarDuration = SnackbarDuration.Short
    ) : SnackbarMessage()

    /**
     * Warning message
     */
    data class Warning(
        override val message: String,
        override val actionLabel: String? = null,
        override val duration: SnackbarDuration = SnackbarDuration.Long
    ) : SnackbarMessage()
}

/**
 * Controller for managing Snackbar messages
 * Centralizes Snackbar logic and provides a clean API
 */
@Stable
class SnackbarController(
    val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) {
    /**
     * Show a snackbar message
     * @param message The message to display
     * @param onAction Optional callback when action is clicked
     */
    fun showMessage(
        message: SnackbarMessage,
        onAction: (() -> Unit)? = null
    ) {
        coroutineScope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message.message,
                actionLabel = message.actionLabel,
                duration = message.duration,
                withDismissAction = true
            )

            if (result == SnackbarResult.ActionPerformed) {
                onAction?.invoke()
            }
        }
    }

    /**
     * Show a success message
     */
    fun showSuccess(message: String, actionLabel: String? = null) {
        showMessage(Success(message, actionLabel))
    }

    /**
     * Show an error message
     */
    fun showError(message: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
        showMessage(Error(message, actionLabel), onAction)
    }

    /**
     * Show an info message
     */
    fun showInfo(message: String, actionLabel: String? = null) {
        showMessage(Info(message, actionLabel))
    }

    /**
     * Show a warning message
     */
    fun showWarning(message: String, actionLabel: String? = null) {
        showMessage(Warning(message, actionLabel))
    }
}

/**
 * Remember a SnackbarController following Compose best practices
 */
@Composable
fun rememberSnackbarController(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): SnackbarController {
    return remember(snackbarHostState, coroutineScope) {
        SnackbarController(snackbarHostState, coroutineScope)
    }
}
