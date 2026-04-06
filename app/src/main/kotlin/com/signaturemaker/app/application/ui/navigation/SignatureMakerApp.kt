package com.signaturemaker.app.application.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.signaturemaker.app.application.core.util.FeatureFlags
import com.signaturemaker.app.application.features.changelog.ChangelogRoute
import com.signaturemaker.app.application.features.files.FilesRoute
import com.signaturemaker.app.application.features.main.MainRoute
import com.signaturemaker.app.application.features.main.MainScreenAction
import com.signaturemaker.app.application.features.onboarding.OnboardingScreen
import com.signaturemaker.app.application.features.settings.SettingsRoute
import com.signaturemaker.app.application.features.sign.SignRoute
import com.signaturemaker.app.application.ui.navigation.core.navigateTo
import com.signaturemaker.app.application.ui.navigation.routes.SignatureMakerRoutes
import com.signaturemaker.app.application.ui.theming.SignatureMakerAppTheme

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
    val context = androidx.compose.ui.platform.LocalContext.current

    // State-based onboarding: when flipped to false, the whole tree recomposes to main app
    var isOnboarding by rememberSaveable {
        mutableStateOf(FeatureFlags.shouldShowOnboarding(context))
    }

    if (isOnboarding) {
        SignatureMakerAppTheme {
            OnboardingScreen(
                onFinish = {
                    FeatureFlags.completeOnboarding(context)
                    isOnboarding = false
                }
            )
        }
    } else {
        MainApp()
    }
}

/**
 * Main application flow with scaffold, drawer, and content navigation.
 * Separated from onboarding so each has its own NavController lifecycle.
 */
@Composable
private fun MainApp() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current
    val activity = context as? android.app.Activity

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
                navController.navigateTo(SignatureMakerRoutes.Settings)
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
                // TODO: Use in-app OSS licenses screen when available
            }

            is MainScreenAction.NavigateToPrivacyPolicy -> {
                com.signaturemaker.app.application.core.util.IntentUtils.openUrlInCustomTab(
                    context = context,
                    url = com.signaturemaker.app.application.core.util.IntentUtils.URL_PRIVACY
                )
            }

            is MainScreenAction.NavigateToEditPrivacyPolicy -> {
                // TODO: Implement Edit Privacy Policy action
            }
        }
    }

    // Handle back press: close app if we're on the start destination
    androidx.activity.compose.BackHandler {
        if (navController.previousBackStackEntry == null) {
            // We're at the root, finish the activity
            activity?.finish()
        } else {
            // Navigate back normally
            navController.popBackStack()
        }
    }

    // Single MainRoute instance wrapping the content navigation
    MainRoute(
        navController = navController,
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
            SignRoute(
                modifier = Modifier.fillMaxSize(),
                onNavigate = { action ->
                    when (action) {
                        is com.signaturemaker.app.application.features.sign.SignScreenAction.NavigateToGallery -> {
                            navController.navigateTo(SignatureMakerRoutes.Files)
                        }
                    }
                }
            )
        }

        // File screen destination
        composable(SignatureMakerRoutes.Files.route) {
            FilesRoute(modifier = Modifier.fillMaxSize())
        }

        // Changelog screen destination
        composable(SignatureMakerRoutes.Changelog.route) {
            ChangelogRoute(modifier = Modifier.fillMaxSize())
        }

        // Settings screen destination
        composable(SignatureMakerRoutes.Settings.route) {
            SettingsRoute(modifier = Modifier.fillMaxSize())
        }
    }
}
