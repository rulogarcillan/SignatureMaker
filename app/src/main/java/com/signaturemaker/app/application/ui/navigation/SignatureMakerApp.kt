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
                // TODO: Implement Send Feedback action
            }

            is MainScreenAction.NavigateToChangeLog -> {
                navController.navigateTo(SignatureMakerRoutes.Changelog)
            }

            is MainScreenAction.NavigateToRateUs -> {
                // TODO: Implement Rate Us action
            }

            is MainScreenAction.NavigateToMoreApps -> {
                // TODO: Implement More Apps action
            }

            is MainScreenAction.NavigateToLicenses -> {
                // TODO: Implement Licenses navigation
            }

            is MainScreenAction.NavigateToPrivacyPolicy -> {
                // TODO: Implement Privacy Policy navigation
            }

            is MainScreenAction.NavigateToEditPrivacyPolicy -> {
                // TODO: Implement Edit Privacy Policy navigation
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

