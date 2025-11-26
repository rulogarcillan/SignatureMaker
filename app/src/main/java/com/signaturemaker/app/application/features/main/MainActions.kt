package com.signaturemaker.app.application.features.main

import androidx.compose.runtime.Stable
import com.signaturemaker.app.application.features.gallery.GalleryDestination
import com.signaturemaker.app.application.features.main.MainAction.NavigateToChangeLog
import com.signaturemaker.app.application.features.main.MainAction.NavigateToEditPrivacyPolicy
import com.signaturemaker.app.application.features.main.MainAction.NavigateToGallery
import com.signaturemaker.app.application.features.main.MainAction.NavigateToLicenses
import com.signaturemaker.app.application.features.main.MainAction.NavigateToMoreApps
import com.signaturemaker.app.application.features.main.MainAction.NavigateToPrivacyPolicy
import com.signaturemaker.app.application.features.main.MainAction.NavigateToRateUs
import com.signaturemaker.app.application.features.main.MainAction.NavigateToSendFeedback
import com.signaturemaker.app.application.features.main.MainAction.NavigateToSettings
import com.signaturemaker.app.application.features.main.MainAction.NavigateToSign
import com.signaturemaker.app.application.features.sign.SignDestination

/*
 * Sealed class representing all possible actions in the Main Screen
 * Follows State Hoisting and Unidirectional Data Flow patterns
 */
@Stable
sealed class MainAction {
    /*
     * Navigation actions
     */
    data object NavigateToSign : MainAction()
    data object NavigateToGallery : MainAction()
    data object NavigateToSettings : MainAction()
    data object NavigateToSendFeedback : MainAction()
    data object NavigateToChangeLog : MainAction()
    data object NavigateToRateUs : MainAction()
    data object NavigateToMoreApps : MainAction()
    data object NavigateToLicenses : MainAction()
    data object NavigateToPrivacyPolicy : MainAction()
    data object NavigateToEditPrivacyPolicy : MainAction()
}

/**
 * Handle MainAction events and execute navigation or business logic
 * Follows unidirectional data flow pattern
 *
 * @param action The action to handle
 * @param mainState The UI state to interact with for navigation
 */
fun handleMainAction(
    action: MainAction,
    mainState: MainUIState
) {
    when (action) {
        is NavigateToSign -> {
            mainState.navigateTo(SignDestination.route)
        }
        is NavigateToGallery -> {
            mainState.navigateTo(GalleryDestination.route)
        }
        is NavigateToSettings -> {
            // TODO: Implement Settings navigation
        }
        is NavigateToSendFeedback -> {
            // TODO: Implement Send Feedback action
        }
        is NavigateToChangeLog -> {
            // TODO: Implement ChangeLog navigation
        }
        is NavigateToRateUs -> {
            // TODO: Implement Rate Us action
        }
        is NavigateToMoreApps -> {
            // TODO: Implement More Apps action
        }
        is NavigateToLicenses -> {
            // TODO: Implement Licenses navigation
        }
        is NavigateToPrivacyPolicy -> {
            // TODO: Implement Privacy Policy navigation
        }
        is NavigateToEditPrivacyPolicy -> {
            // TODO: Implement Edit Privacy Policy navigation
        }
    }
}

/**
 * Convert MainMenuItem to MainAction
 * Helper function to map menu items to their corresponding actions
 */
fun MainMenuItem.toAction(): MainAction {
    return when (this) {
        is MainMenuItem.Sign -> NavigateToSign
        is MainMenuItem.Gallery -> NavigateToGallery
        is MainMenuItem.Settings -> NavigateToSettings
        is MainMenuItem.SendFeedback -> NavigateToSendFeedback
        is MainMenuItem.ChangeLog -> NavigateToChangeLog
        is MainMenuItem.RateUs -> NavigateToRateUs
        is MainMenuItem.MoreApps -> NavigateToMoreApps
        is MainMenuItem.Licenses -> NavigateToLicenses
        is MainMenuItem.PrivacyPolicy -> NavigateToPrivacyPolicy
        is MainMenuItem.EditPrivacyPolicy -> NavigateToEditPrivacyPolicy
    }
}

