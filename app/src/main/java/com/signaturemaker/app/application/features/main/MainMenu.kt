package com.signaturemaker.app.application.features.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import com.signaturemaker.app.R
import com.signaturemaker.app.application.features.main.MainMenuItem.ChangeLog
import com.signaturemaker.app.application.features.main.MainMenuItem.Files
import com.signaturemaker.app.application.features.main.MainMenuItem.PrivacyPolicy
import com.signaturemaker.app.application.features.main.MainMenuItem.RateUs
import com.signaturemaker.app.application.features.main.MainMenuItem.Settings
import com.signaturemaker.app.application.features.main.MainMenuItem.Sign

/*
 * Menu configuration for Main Screen following Compose best practices
 * Immutable configuration with available menu items
 */
@Immutable
data class MainMenuConfig private constructor(
    val menuItems: List<MainMenuItem>,
) {
    companion object {
        fun create(): MainMenuConfig {
            return MainMenuConfig(
                menuItems = listOf(
                    Sign,
                    Files,
                    Settings,
                    //SendFeedback,
                    ChangeLog,
                    RateUs,
                    //MoreApps,
                    //Licenses,
                    PrivacyPolicy,
                    //EditPrivacyPolicy,
                )
            )
        }
    }
}

/*
 * Remember MainMenuConfig following Compose best practices
 */
@Composable
fun rememberMainMenuConfig(): MainMenuConfig {
    return remember { MainMenuConfig.create() }
}

/*
 * Main Menu Items - Sealed class representing all available menu options
 */
@Immutable
sealed class MainMenuItem(@StringRes val titleResId: Int, val icon: ImageVector) {
    data object Sign : MainMenuItem(R.string.title_create_signature, Icons.Default.Create)
    data object Files : MainMenuItem(R.string.title_gallery, Icons.Default.FolderOpen)
    data object Settings : MainMenuItem(R.string.title_setting, Icons.Default.Settings)

    // data object SendFeedback : MainMenuItem(R.string.send_suggestions, Icons.Default.Feedback)
    data object ChangeLog : MainMenuItem(R.string.changelog, Icons.AutoMirrored.Filled.Article)
    data object RateUs : MainMenuItem(R.string.rate, Icons.Default.StarRate)

    //data object MoreApps : MainMenuItem(R.string.more_app, Icons.Default.Shop)
    //data object Licenses : MainMenuItem(R.string.license, Icons.Default.Copyright)
    data object PrivacyPolicy : MainMenuItem(R.string.privacy_policy, Icons.Default.PrivacyTip)
    //data object EditPrivacyPolicy : MainMenuItem(R.string.title_edit_privacy_policy, Icons.Default.Badge)
}
