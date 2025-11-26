package com.signaturemaker.app.application.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.signaturemaker.app.application.features.changelog.ChangelogRoute
import com.signaturemaker.app.application.features.files.FilesRoute
import com.signaturemaker.app.application.features.main.MainRoute
import com.signaturemaker.app.application.features.main.MainScreenAction
import com.signaturemaker.app.application.features.sign.SignRoute
import com.signaturemaker.app.application.ui.navigation.core.navigateTo
import com.signaturemaker.app.application.ui.navigation.routes.SignatureMakerRoutes

/**
 * SignatureMaker App
 * Entry point for the entire application UI.
 *
 * Architecture:
 * - Creates a single MainScreen (scaffold + drawer) instance
 * - Hosts the content navigation (ContentNavHost) inside the scaffold
 * - Maintains drawer state and selected menu across navigation
 */
@Composable
fun SignatureMakerApp() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current

    // Navigation action handler for drawer menu actions
    val onMainNavigationAction: (MainScreenAction) -> Unit = { action ->
        when (action) {
            is MainScreenAction.NavigateToSign -> {
                navController.navigateTo(SignatureMakerRoutes.Sign)
            }

            is MainScreenAction.NavigateToFiles -> {
                navController.navigateTo(SignatureMakerRoutes.Files)
            }

            is MainScreenAction.NavigateToSettings -> {
                // TODO: Implement Settings navigation
            }

            is MainScreenAction.NavigateToSendFeedback -> {
                com.signaturemaker.app.application.core.util.IntentUtils.sendFeedbackEmail(context)
            }

            is MainScreenAction.NavigateToChangeLog -> {
                navController.navigateTo(SignatureMakerRoutes.Changelog)
            }

            is MainScreenAction.NavigateToRateUs -> {
                com.signaturemaker.app.application.core.util.IntentUtils.openRateApp(context)
            }

            is MainScreenAction.NavigateToMoreApps -> {
                com.signaturemaker.app.application.core.util.IntentUtils.openMoreApps(context)
            }

            is MainScreenAction.NavigateToLicenses -> {
                // Open OSS Licenses Activity from Google Play Services
                val intent = android.content.Intent(
                    context,
                    com.google.android.gms.oss.licenses.OssLicensesMenuActivity::class.java
                )
                context.startActivity(intent)
            }

            is MainScreenAction.NavigateToPrivacyPolicy -> {
                com.signaturemaker.app.application.core.util.IntentUtils.openUrlInCustomTab(
                    context = context,
                    url = com.signaturemaker.app.application.core.util.IntentUtils.URL_PRIVACY
                )
            }

            is MainScreenAction.NavigateToEditPrivacyPolicy -> {
                // Open GDPR/UMP Privacy Options Form
                if (context is android.app.Activity) {
                    com.google.android.ump.UserMessagingPlatform.showPrivacyOptionsForm(context) { formError ->
                        formError?.let {
                            // Log error if needed
                            android.util.Log.e("UMP", "Privacy options form error: ${it.message}")
                        }
                    }
                }
            }
        }
    }

    // Single MainRoute instance wrapping the content navigation
    MainRoute(
        onNavigationAction = onMainNavigationAction
    ) { paddingValues ->
        // Content navigation inside the scaffold
        ContentNavHost(
            navController = navController,
            paddingValues = paddingValues
        )
    }
}

/**
 * Content Navigation Host
 * Manages navigation between content screens (Sign, Gallery, etc.)
 * This is rendered inside the MainScreen's scaffold content area.
 *
 * @param navController The NavController for managing navigation between content screens
 * @param paddingValues PaddingValues from the parent scaffold
 */
@Composable
private fun ContentNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = SignatureMakerRoutes.Sign.route,
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        // Sign screen destination
        composable(SignatureMakerRoutes.Sign.route) {
            SignRoute(modifier = Modifier.fillMaxSize())
        }

        // File screen destination
        composable(SignatureMakerRoutes.Files.route) {
            FilesRoute(modifier = Modifier.fillMaxSize())
        }

        // Changelog screen destination
        composable(SignatureMakerRoutes.Changelog.route) {
            ChangelogRoute(modifier = Modifier.fillMaxSize())
        }
    }
}

