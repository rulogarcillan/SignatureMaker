package com.signaturemaker.app.application.features.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.signaturemaker.app.application.ui.navigation.routes.SignatureMakerRoutes

@Composable
fun MainRoute(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onNavigationAction: (MainScreenAction) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val mainState = rememberMainState()
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    // Observe navigation changes and update menu selection
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            val route = backStackEntry.destination.route
            val menuItem = when (route) {
                SignatureMakerRoutes.Sign.route -> MainMenuItem.Sign
                SignatureMakerRoutes.Files.route -> MainMenuItem.Files
                SignatureMakerRoutes.Settings.route -> MainMenuItem.Settings
                SignatureMakerRoutes.Changelog.route -> MainMenuItem.ChangeLog
                else -> null
            }
            menuItem?.let { mainState.changeMenuSelected(it) }
        }
    }

    // Observe lifecycle: reset menu when returning from external apps
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // App resumed from background, sync menu with current route
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                val menuItem = when (currentRoute) {
                    SignatureMakerRoutes.Sign.route -> MainMenuItem.Sign
                    SignatureMakerRoutes.Files.route -> MainMenuItem.Files
                    SignatureMakerRoutes.Settings.route -> MainMenuItem.Settings
                    SignatureMakerRoutes.Changelog.route -> MainMenuItem.ChangeLog
                    else -> MainMenuItem.Sign // Default to Sign if unknown
                }
                mainState.changeMenuSelected(menuItem)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    MainScreen(
        modifier = modifier,
        mainState = mainState,
        onNavigationAction = onNavigationAction,
        content = content
    )
}
