package com.signaturemaker.app

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

@Immutable
data class MainActivityUIState(
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


sealed class MainActivityMenu(val title: String, val icon: ImageVector) {
    data object Sign : MainActivityMenu("Create signature", Icons.Default.ModeEditOutline)
    data object Gallery : MainActivityMenu("Gallery", Icons.Default.BrowseGallery)
    data object Settings : MainActivityMenu("Settings", Icons.Default.Settings)
    data object SendFeedback : MainActivityMenu("Send feedback", Icons.Default.Feedback)
    data object ChangeLog : MainActivityMenu("Change log", Icons.Default.Feed)
    data object RateUs : MainActivityMenu("Rate us", Icons.Default.Star)
    data object MoreApps : MainActivityMenu("More apps", Icons.Default.Shop)
    data object Licenses : MainActivityMenu("Licenses", Icons.Default.Copyright)
    data object PrivacyPolicy : MainActivityMenu("Privacy policy", Icons.Default.Fingerprint)
    data object EditPrivacyPolicy : MainActivityMenu("Edit privacy policy", Icons.Default.Badge)

}