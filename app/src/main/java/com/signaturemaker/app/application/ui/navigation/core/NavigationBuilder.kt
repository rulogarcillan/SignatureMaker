package com.signaturemaker.app.application.ui.navigation.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * DSL for building navigation destinations with type safety and minimal boilerplate.
 * Navigation is handled through action lambdas, keeping the NavController encapsulated.
 */

/**
 * Add a simple destination without arguments.
 * The content receives no parameters - navigation is handled through action callbacks.
 */
fun NavGraphBuilder.destination(
    route: SimpleRoute,
    content: @Composable () -> Unit
) {
    composable(route.route) {
        content()
    }
}

/**
 * Add a destination with typed arguments.
 * The content receives the extracted arguments - navigation is handled through action callbacks.
 */
fun <T> NavGraphBuilder.destination(
    route: ArgumentRoute<T>,
    content: @Composable (args: T?) -> Unit
) {
    composable(
        route = route.route,
        arguments = route.arguments
    ) { backStackEntry ->
        val args = route.extractArguments(backStackEntry)
        content(args)
    }
}
