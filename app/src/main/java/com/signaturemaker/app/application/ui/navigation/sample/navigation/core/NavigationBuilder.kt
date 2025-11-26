package com.kubit.charts.storybook.ui.navigation.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

/**
 * DSL for building navigation destinations with type safety and minimal boilerplate.
 */

/**
 * Add a simple destination without arguments.
 */
fun NavGraphBuilder.destination(
    route: SimpleRoute,
    navController: NavHostController,
    content: @Composable (NavHostController) -> Unit
) {
    composable(route.route) {
        content(navController)
    }
}

/**
 * Add a destination with typed arguments.
 */
fun <T> NavGraphBuilder.destination(
    route: ArgumentRoute<T>,
    navController: NavHostController,
    content: @Composable (args: T?, navController: NavHostController) -> Unit
) {
    composable(
        route = route.route,
        arguments = route.arguments
    ) { backStackEntry ->
        val args = route.extractArguments(backStackEntry)
        content(args, navController)
    }
}
