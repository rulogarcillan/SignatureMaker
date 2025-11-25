package com.signaturemaker.app

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material.icons.filled.Copyright
import androidx.compose.material.icons.filled.Feed
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.ModeEditOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Main Activity UI State
 */
@Immutable
data class MainActivityUIState private constructor(
    val menu: List<MainActivityMenu>,
) {
    companion object {
        fun create(): MainActivityUIState {
            return MainActivityUIState(
                menu = listOf(
                    MainActivityMenu.Sign,
                    MainActivityMenu.Gallery,
                    MainActivityMenu.Settings,
                    MainActivityMenu.SendFeedback,
                    MainActivityMenu.ChangeLog,
                    MainActivityMenu.RateUs,
                    MainActivityMenu.MoreApps,
                    MainActivityMenu.Licenses,
                    MainActivityMenu.PrivacyPolicy,
                    MainActivityMenu.EditPrivacyPolicy,
                )
            )
        }
    }
}

/**
 * Main Activity Menu
 */
@Immutable
sealed class MainActivityMenu(@StringRes val titleResId: Int, val icon: ImageVector) {
    data object Sign : MainActivityMenu(R.string.title_create_signature, Icons.Default.ModeEditOutline)
    data object Gallery : MainActivityMenu(R.string.title_gallery, Icons.Default.BrowseGallery)
    data object Settings : MainActivityMenu(R.string.title_setting, Icons.Default.Settings)
    data object SendFeedback : MainActivityMenu(R.string.send_suggestions, Icons.Default.Feedback)
    data object ChangeLog : MainActivityMenu(R.string.changelog, Icons.Default.Feed)
    data object RateUs : MainActivityMenu(R.string.rate, Icons.Default.Star)
    data object MoreApps : MainActivityMenu(R.string.more_app, Icons.Default.Shop)
    data object Licenses : MainActivityMenu(R.string.license, Icons.Default.Copyright)
    data object PrivacyPolicy : MainActivityMenu(R.string.privacy_policy, Icons.Default.Fingerprint)
    data object EditPrivacyPolicy : MainActivityMenu(R.string.title_edit_privacy_policy, Icons.Default.Badge)
}
