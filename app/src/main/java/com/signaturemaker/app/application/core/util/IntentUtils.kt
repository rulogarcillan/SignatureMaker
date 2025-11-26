package com.signaturemaker.app.application.core.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.signaturemaker.app.application.utils.Constants.URL_RATE
import com.tuppersoft.skizo.android.core.extension.loge

/**
 * Utility object for opening external links and intents
 */
object IntentUtils {

    // URLs
    const val URL_PRIVACY = "https://info.tuppersoft.com/privacy/privacy_policy_signature.html"
    const val URL_LINKEDIN = "https://www.linkedin.com/in/raul-rodriguez-concepcion/"
    const val URL_GITHUB = "https://github.com/rulogarcillan"
    const val URL_TWITTER = "https://twitter.com/tuppersoft"
    const val URL_TELEGRAM_ES = "https://telegram.me/signature_maker_es"
    const val URL_TELEGRAM_EN = "https://telegram.me/signature_maker_eng"

    // Package names
    private const val PACKAGE_TWITTER = "com.twitter.android"
    private const val PACKAGE_LINKEDIN = "com.linkedin.android"
    private const val PACKAGE_GITHUB = "com.github.android"
    private const val PACKAGE_TELEGRAM = "org.telegram.messenger"

    /**
     * Open URL in Chrome Custom Tabs with app theme colors
     */
    fun openUrlInCustomTab(
        context: Context,
        url: String,
        toolbarColor: Color? = null,
        navigationBarColor: Color? = null
    ) {
        try {
            val builder = CustomTabsIntent.Builder()

            toolbarColor?.let {
                builder.setToolbarColor(it.toArgb())
            }

            navigationBarColor?.let {
                builder.setNavigationBarColor(it.toArgb())
            }

            val customTabsIntent = builder.build()
            // NO agregar flags que fuercen a Android a serializar el estado de la Activity
            // Los flags FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS y FLAG_ACTIVITY_NO_HISTORY
            // causan que Android intente guardar el estado completo (incluyendo Bitmaps)
            // lo cual falla con "Could not copy bitmap to parcel blob"

            customTabsIntent.launchUrl(context, Uri.parse(url))
        } catch (e: Exception) {
            e.message?.loge()
            // Fallback to browser
            openUrlInBrowser(context, url)
        }
    }

    /**
     * Open URL in default browser
     */
    fun openUrlInBrowser(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.message?.loge()
        }
    }

    /**
     * Open app in Play Store for rating
     */
    fun openRateApp(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(URL_RATE)
            context.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
        }
    }

    /**
     * Open developer's apps in Play Store
     */
    fun openMoreApps(context: Context, developerName: String = "Raúl R.") {
        try {
            val uri = Uri.parse("market://search?q=pub:$developerName")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Fallback to web Play Store
            val uri = Uri.parse("https://play.google.com/store/search?q=pub:$developerName")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }
    }

    /**
     * Open LinkedIn profile
     */
    fun openLinkedIn(context: Context, username: String = "raul-rodriguez-concepcion") {
        try {
            // Try to open in LinkedIn app
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://$username"))
            intent.setPackage(PACKAGE_LINKEDIN)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Fallback to web
            openUrlInBrowser(context, "https://www.linkedin.com/in/$username/")
        }
    }

    /**
     * Open GitHub profile
     */
    fun openGitHub(context: Context, username: String = "rulogarcillan") {
        openUrlInBrowser(context, "https://github.com/$username")
    }

    /**
     * Open Twitter/X profile
     */
    fun openTwitter(context: Context, username: String = "tuppersoft") {
        try {
            // Try to open in Twitter app
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=$username"))
            intent.setPackage(PACKAGE_TWITTER)
            // No usar FLAG_ACTIVITY_NEW_TASK para mantener el estado de la app
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Fallback to web
            openUrlInBrowser(context, "https://twitter.com/$username")
        }
    }

    /**
     * Open Telegram channel based on locale
     */
    fun openTelegram(context: Context, isSpanish: Boolean) {
        val url = if (isSpanish) URL_TELEGRAM_ES else URL_TELEGRAM_EN
        try {
            // Try to open in Telegram app
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.setPackage(PACKAGE_TELEGRAM)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Fallback to web
            openUrlInBrowser(context, url)
        }
    }

    /**
     * Send email with feedback
     */
    fun sendFeedbackEmail(
        context: Context,
        email: String = "support@tuppersoft.com",
        subject: String = "Feedback - Signature Maker"
    ) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }
            context.startActivity(Intent.createChooser(intent, "Send feedback"))
        } catch (e: ActivityNotFoundException) {
            e.message?.loge()
        }
    }
}

