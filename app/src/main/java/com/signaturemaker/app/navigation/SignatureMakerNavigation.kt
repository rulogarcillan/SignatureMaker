package com.signaturemaker.app.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.signaturemaker.app.features.sign.SignRoute
import com.signaturemaker.app.features.sign.SignDestination


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SignatureMakerNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = SignDestination.route
    ) {
        addSignRoute(navController)
    }
}

@ExperimentalFoundationApi
private fun NavGraphBuilder.addSignRoute(navController: NavHostController) {
    composable(SignDestination.route) {
        SignRoute()
    }
}
