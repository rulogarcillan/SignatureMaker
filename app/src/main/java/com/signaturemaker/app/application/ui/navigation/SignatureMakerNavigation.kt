package com.signaturemaker.app.application.ui.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.signaturemaker.app.application.features.gallery.GalleryDestination
import com.signaturemaker.app.application.features.gallery.GalleryRoute
import com.signaturemaker.app.application.features.sign.SignDestination
import com.signaturemaker.app.application.features.sign.SignRoute

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SignatureMakerNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = SignDestination.route
    ) {
        addSignRoute(navController)
        addGalleryRoute(navController)
    }
}

@ExperimentalFoundationApi
private fun NavGraphBuilder.addSignRoute(navController: NavHostController) {
    composable(SignDestination.route) {
        SignRoute()
    }
}

@ExperimentalFoundationApi
private fun NavGraphBuilder.addGalleryRoute(navController: NavHostController) {
    composable(GalleryDestination.route) {
        GalleryRoute()
    }
}
