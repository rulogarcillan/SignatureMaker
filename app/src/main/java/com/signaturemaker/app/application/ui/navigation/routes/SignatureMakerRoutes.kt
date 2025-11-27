package com.signaturemaker.app.application.ui.navigation.routes

import com.signaturemaker.app.application.ui.navigation.core.SimpleRoute

/**
 * Sealed class containing all navigation routes for the SignatureMaker app.
 * This provides type-safe navigation with minimal boilerplate.
 */
sealed class SignatureMakerRoutes {

    /**
     * Main screen route - entry point with drawer
     */
    data object Main : SimpleRoute {
        override val route: String = "mainScreen"
    }

    /**
     * Sign screen route - signature creation
     */
    data object Sign : SimpleRoute {
        override val route: String = "signScreen"
    }

    /**
     * File screen route
     */
    data object Files : SimpleRoute {
        override val route: String = "fileScreen"
    }

    /**
     * Changelog screen route
     */
    data object Changelog : SimpleRoute {
        override val route: String = "changelogScreen"
    }

    /**
     * Settings screen route
     */
    data object Settings : SimpleRoute {
        override val route: String = "settingsScreen"
    }

    /**
     * Example of a route with typed arguments.
     * Uncomment and adapt when you need to pass arguments to a screen.
     *
     * Usage:
     * ```
     * // Navigate to the screen
     * navController.navigateTo(
     *     route = SignatureMakerRoutes.SignatureDetails,
     *     args = SignatureMakerRoutes.SignatureDetails.Args(signatureId = "123")
     * )
     *
     * // In NavHost
     * destination(
     *     route = SignatureMakerRoutes.SignatureDetails,
     *     navController = navController
     * ) { args, navController ->
     *     args?.let { SignatureDetailsScreen(it.signatureId) }
     * }
     * ```
     */
    /*
    data object SignatureDetails : ArgumentRoute<SignatureDetails.Args> {
        data class Args(val signatureId: String)

        private const val SIGNATURE_ID_ARG = "signatureId"
        override val route: String = "signature_details/{$SIGNATURE_ID_ARG}"

        override val arguments = buildArguments {
            stringArg(SIGNATURE_ID_ARG)
        }

        override fun createRoute(args: Args): String {
            return "signature_details/${args.signatureId}"
        }

        override fun extractArguments(backStackEntry: NavBackStackEntry): Args? {
            val signatureId = backStackEntry.arguments?.getString(SIGNATURE_ID_ARG)
            return signatureId?.let { Args(it) }
        }
    }
    */
}

