package com.kubit.charts.storybook.ui.navigation.core

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Base sealed interface for all navigation routes in the app.
 * Provides type-safe navigation with minimal boilerplate.
 */
sealed interface NavigationRoute {
    val route: String
    val arguments: List<NamedNavArgument> get() = emptyList()
}

/**
 * Interface for routes that don't require arguments.
 */
interface SimpleRoute : NavigationRoute

/**
 * Interface for routes that require typed arguments.
 * @param T The type of arguments this route expects
 */
interface ArgumentRoute<T> : NavigationRoute {
    fun createRoute(args: T): String
    fun extractArguments(backStackEntry: NavBackStackEntry): T?
}

/**
 * DSL builder for creating navigation arguments with type safety.
 */
class ArgumentBuilder {
    private val _arguments = mutableListOf<NamedNavArgument>()

    val arguments: List<NamedNavArgument> get() = _arguments.toList()

    // String types
    fun stringArg(name: String, isOptional: Boolean = false) {
        _arguments.add(
            navArgument(name) {
                type = NavType.StringType
                nullable = isOptional
            }
        )
    }

    fun stringArrayArg(name: String, isOptional: Boolean = false) {
        _arguments.add(
            navArgument(name) {
                type = NavType.StringArrayType
                nullable = isOptional
            }
        )
    }

    fun stringListArg(name: String, isOptional: Boolean = false) {
        _arguments.add(
            navArgument(name) {
                type = NavType.StringListType
                nullable = isOptional
            }
        )
    }

    // Int types
    fun intArg(name: String, isOptional: Boolean = false) {
        _arguments.add(
            navArgument(name) {
                type = NavType.IntType
                nullable = isOptional
            }
        )
    }

    fun intArrayArg(name: String, isOptional: Boolean = false) {
        _arguments.add(
            navArgument(name) {
                type = NavType.IntArrayType
                nullable = isOptional
            }
        )
    }

    fun intListArg(name: String, isOptional: Boolean = false) {
        _arguments.add(
            navArgument(name) {
                type = NavType.IntListType
                nullable = isOptional
            }
        )
    }

    // Long types
    fun longArg(name: String, isOptional: Boolean = false) {
        _arguments.add(
            navArgument(name) {
                type = NavType.LongType
                nullable = isOptional
            }
        )
    }

    fun longArrayArg(name: String, isOptional: Boolean = false) {
        _arguments.add(
            navArgument(name) {
                type = NavType.LongArrayType
                nullable = isOptional
            }
        )
    }

    fun longListArg(name: String, isOptional: Boolean = false) {
        _arguments.add(
            navArgument(name) {
                type = NavType.LongListType
                nullable = isOptional
            }
        )
    }

    // Float types
    fun floatArg(name: String, isOptional: Boolean = false) {
        _arguments.add(
            navArgument(name) {
                type = NavType.FloatType
                nullable = isOptional
            }
        )
    }

    fun floatArrayArg(name: String, isOptional: Boolean = false) {
        _arguments.add(
            navArgument(name) {
                type = NavType.FloatArrayType
                nullable = isOptional
            }
        )
    }

    fun floatListArg(name: String, isOptional: Boolean = false) {
        _arguments.add(
            navArgument(name) {
                type = NavType.FloatListType
                nullable = isOptional
            }
        )
    }

    // Boolean types
    fun boolArg(name: String, isOptional: Boolean = false) {
        _arguments.add(
            navArgument(name) {
                type = NavType.BoolType
                nullable = isOptional
            }
        )
    }

    fun boolArrayArg(name: String, isOptional: Boolean = false) {
        _arguments.add(
            navArgument(name) {
                type = NavType.BoolArrayType
                nullable = isOptional
            }
        )
    }

    fun boolListArg(name: String, isOptional: Boolean = false) {
        _arguments.add(
            navArgument(name) {
                type = NavType.BoolListType
                nullable = isOptional
            }
        )
    }
}

/**
 * Helper function to build arguments with DSL syntax.
 */
fun buildArguments(builder: ArgumentBuilder.() -> Unit): List<NamedNavArgument> {
    return ArgumentBuilder().apply(builder).arguments
}
