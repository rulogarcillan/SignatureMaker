package com.signaturemaker.app.application.ui.navigation.routes

import androidx.navigation.NavBackStackEntry
import com.signaturemaker.app.application.ui.navigation.core.ArgumentRoute
import com.signaturemaker.app.application.ui.navigation.core.SimpleRoute
import com.signaturemaker.app.application.ui.navigation.core.buildArguments

/**
 * Sealed class containing all navigation routes for the SignatureMaker app.
 * This provides type-safe navigation with minimal boilerplate.
 */
sealed class SignatureMakerRoutes {

    /**
     * Main screen route - entry point with drawer
     */
    data object Main : SimpleRoute {
        override val route: String = "main"
    }

    /**
     * Sign screen route - signature creation
     */
    data object Sign : SimpleRoute {
        override val route: String = "signscreen"
    }

    /**
     * Gallery screen route
     */
    data object Gallery : SimpleRoute {
        override val route: String = "galleryscreen"
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

