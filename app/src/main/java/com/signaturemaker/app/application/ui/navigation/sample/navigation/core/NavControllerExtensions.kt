package com.kubit.charts.storybook.ui.navigation.core

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

/**
 * Type-safe navigation extensions for NavController.
 * Provides a clean API for navigating with typed arguments.
 */

/**
 * Navigate to a simple route without arguments.
 */
fun NavController.navigateTo(
    route: SimpleRoute,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route.route, builder)
}

/**
 * Navigate to a route with typed arguments.
 */
fun <T> NavController.navigateTo(
    route: ArgumentRoute<T>,
    args: T,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route.createRoute(args), builder)
}

/**
 * Navigate back to previous destination.
 */
fun NavController.navigateBack(): Boolean {
    return navigateUp()
}

/**
 * Navigate back to a specific route, clearing the stack up to that point.
 */
fun NavController.navigateBackTo(
    route: NavigationRoute,
    inclusive: Boolean = false
) {
    popBackStack(route.route, inclusive)
}
