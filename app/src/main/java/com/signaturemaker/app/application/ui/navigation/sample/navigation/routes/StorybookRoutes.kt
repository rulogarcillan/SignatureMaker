package com.kubit.charts.storybook.ui.navigation.routes

import androidx.navigation.NavBackStackEntry
import com.kubit.charts.storybook.domain.model.Component
import com.kubit.charts.storybook.domain.model.Sample
import com.kubit.charts.storybook.ui.navigation.core.ArgumentRoute
import com.kubit.charts.storybook.ui.navigation.core.SimpleRoute
import com.kubit.charts.storybook.ui.navigation.core.buildArguments
import com.kubit.charts.storybook.ui.navigation.routes.StorybookRoutes.ComponentDetails.COMPONENT_ARG

/**
 * Sealed class containing all navigation routes for the Storybook app.
 * This provides type-safe navigation with minimal boilerplate.
 */
sealed class StorybookRoutes {

    /**
     * Main screen route - entry point of the app
     */
    data object Main : SimpleRoute {
        override val route: String = "main"
    }

    /**
     * Component list screen route
     */
    data object ComponentList : SimpleRoute {
        override val route: String = "component_list"
    }

    /**
     * Sample list screen route
     */
    data object SampleList : SimpleRoute {
        override val route: String = "sample_list"
    }

    /**
     * Component details screen route with typed arguments
     */
    data object ComponentDetails : ArgumentRoute<ComponentDetails.Args> {

        data class Args(val component: Component)

        private const val COMPONENT_ARG = "component"
        override val route: String = "component_details/{$COMPONENT_ARG}"

        override val arguments = buildArguments {
            stringArg(COMPONENT_ARG)
        }

        override fun createRoute(args: Args): String {
            return "component_details/${args.component.name}"
        }

        override fun extractArguments(backStackEntry: NavBackStackEntry): Args? {
            val componentName = backStackEntry.arguments?.getString(COMPONENT_ARG)
            return componentName?.let { name ->
                try {
                    Args(Component.valueOf(name))
                } catch (e: IllegalArgumentException) {
                    null
                }
            }
        }
    }
    /**
     * Component details screen route with typed arguments
     */
    data object SampleDetails : ArgumentRoute<SampleDetails.Args> {

        data class Args(val component: Sample)

        private const val SAMPLE_ARG = "sample"
        override val route: String = "sample_details/{$SAMPLE_ARG}"

        override val arguments = buildArguments {
            stringArg(SAMPLE_ARG)
        }

        override fun createRoute(args: Args): String {
            return "sample_details/${args.component.name}"
        }

        override fun extractArguments(backStackEntry: NavBackStackEntry): Args? {
            val componentName = backStackEntry.arguments?.getString(SAMPLE_ARG)
            return componentName?.let { name ->
                try {
                    Args(Sample.valueOf(name))
                } catch (e: IllegalArgumentException) {
                    null
                }
            }
        }
    }
}
