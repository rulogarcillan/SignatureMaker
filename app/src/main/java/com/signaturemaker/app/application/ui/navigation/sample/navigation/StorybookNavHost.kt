package com.kubit.charts.storybook.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kubit.charts.storybook.ui.navigation.core.navigateBack
import com.kubit.charts.storybook.ui.navigation.core.navigateTo
import com.kubit.charts.storybook.ui.navigation.routes.StorybookRoutes.ComponentDetails
import com.kubit.charts.storybook.ui.navigation.routes.StorybookRoutes.ComponentList
import com.kubit.charts.storybook.ui.navigation.routes.StorybookRoutes.Main
import com.kubit.charts.storybook.ui.navigation.routes.StorybookRoutes.SampleDetails
import com.kubit.charts.storybook.ui.navigation.routes.StorybookRoutes.SampleList
import com.kubit.charts.storybook.ui.screens.componentdetails.ComponentDetailsScreen
import com.kubit.charts.storybook.ui.screens.componentdetails.ComponentDetailsScreenAction
import com.kubit.charts.storybook.ui.screens.componentlist.ComponentListScreen
import com.kubit.charts.storybook.ui.screens.componentlist.ComponentListScreenAction
import com.kubit.charts.storybook.ui.screens.main.MainScreen
import com.kubit.charts.storybook.ui.screens.main.MainScreenAction.NavigateToComponentList
import com.kubit.charts.storybook.ui.screens.main.MainScreenAction.NavigateToSamples
import com.kubit.charts.storybook.ui.screens.sampledetails.SampleDetailsScreen
import com.kubit.charts.storybook.ui.screens.sampledetails.SampleDetailsScreenAction
import com.kubit.charts.storybook.ui.screens.samplelist.SampleListScreen
import com.kubit.charts.storybook.ui.screens.samplelist.SampleListScreenAction

/**
 * Storybook Navigation Host
 * Sets up the navigation graph for the Storybook app
 */
@Suppress("LongMethod")
@Composable
fun StorybookNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Main.route
    ) {
        // Main screen destination
        composable(Main.route) {
            MainScreen(
                onNavigationAction = { action ->
                    when (action) {
                        is NavigateToComponentList -> {
                            navController.navigateTo(ComponentList)
                        }

                        is NavigateToSamples -> {
                            navController.navigateTo(SampleList)
                        }
                    }
                }
            )
        }

        // Component list destination
        composable(ComponentList.route) {
            ComponentListScreen(
                onNavigationAction = { action ->
                    when (action) {
                        is ComponentListScreenAction.NavigateToComponentDetails -> {
                            navController.navigateTo(
                                route = ComponentDetails,
                                args = ComponentDetails.Args(action.component)
                            )
                        }

                        is ComponentListScreenAction.NavigateBack -> {
                            navController.navigateBack()
                        }
                    }
                }
            )
        }

        // Component list destination
        composable(SampleList.route) {
            SampleListScreen(
                onNavigationAction = { action ->
                    when (action) {
                        is SampleListScreenAction.NavigateToSampleDetails -> {
                            navController.navigateTo(
                                route = SampleDetails,
                                args = SampleDetails.Args(action.sample)
                            )
                        }

                        is SampleListScreenAction.NavigateBack -> {
                            navController.navigateBack()
                        }
                    }
                }
            )
        }

        // Component details destination with typed arguments
        composable(
            route = ComponentDetails.route,
            arguments = ComponentDetails.arguments
        ) { backStackEntry ->
            val args = ComponentDetails.extractArguments(backStackEntry)
            args?.let { safeArgs ->
                ComponentDetailsScreen(
                    component = safeArgs.component,
                    onNavigationAction = { action ->
                        when (action) {
                            is ComponentDetailsScreenAction.NavigateBack -> {
                                navController.navigateBack()
                            }
                        }
                    }
                )
            }
        }

        // Component details destination with typed arguments
        composable(
            route = SampleDetails.route,
            arguments = SampleDetails.arguments
        ) { backStackEntry ->
            val args = SampleDetails.extractArguments(backStackEntry)
            args?.let { safeArgs ->
                SampleDetailsScreen(
                    sample = safeArgs.component,
                    onNavigationAction = { action ->
                        when (action) {
                            is SampleDetailsScreenAction.NavigateBack -> {
                                navController.navigateBack()
                            }
                        }
                    }
                )
            }
        }
    }
}
