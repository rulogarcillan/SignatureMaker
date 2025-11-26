package com.signaturemaker.app.application.features.main

import androidx.compose.runtime.Stable
import com.signaturemaker.app.application.features.main.MainScreenAction.NavigateToChangeLog
import com.signaturemaker.app.application.features.main.MainScreenAction.NavigateToEditPrivacyPolicy
import com.signaturemaker.app.application.features.main.MainScreenAction.NavigateToFiles
import com.signaturemaker.app.application.features.main.MainScreenAction.NavigateToLicenses
import com.signaturemaker.app.application.features.main.MainScreenAction.NavigateToPrivacyPolicy
import com.signaturemaker.app.application.features.main.MainScreenAction.NavigateToRateUs
import com.signaturemaker.app.application.features.main.MainScreenAction.NavigateToSettings
import com.signaturemaker.app.application.features.main.MainScreenAction.NavigateToSign

/*
 * Sealed class representing all possible actions in the Main Screen
 * Follows State Hoisting and Unidirectional Data Flow patterns
 */
@Stable
sealed interface MainScreenAction {
    /*
     * Navigation actions
     */
    data object NavigateToSign : MainScreenAction
    data object NavigateToFiles : MainScreenAction
    data object NavigateToSettings : MainScreenAction
    data object NavigateToSendFeedback : MainScreenAction
    data object NavigateToChangeLog : MainScreenAction
    data object NavigateToRateUs : MainScreenAction
    data object NavigateToMoreApps : MainScreenAction
    data object NavigateToLicenses : MainScreenAction
    data object NavigateToPrivacyPolicy : MainScreenAction
    data object NavigateToEditPrivacyPolicy : MainScreenAction
}

/**
 * Convert MainMenuItem to MainAction
 * Helper function to map menu items to their corresponding actions
 */
fun MainMenuItem.toAction(): MainScreenAction {
    return when (this) {
        is MainMenuItem.Sign -> NavigateToSign
        is MainMenuItem.Files -> NavigateToFiles
        is MainMenuItem.Settings -> NavigateToSettings
        //is MainMenuItem.SendFeedback -> NavigateToSendFeedback
        is MainMenuItem.ChangeLog -> NavigateToChangeLog
        is MainMenuItem.RateUs -> NavigateToRateUs
        //is MainMenuItem.MoreApps -> NavigateToMoreApps
        is MainMenuItem.Licenses -> NavigateToLicenses
        is MainMenuItem.PrivacyPolicy -> NavigateToPrivacyPolicy
        is MainMenuItem.EditPrivacyPolicy -> NavigateToEditPrivacyPolicy
    }
}

