package com.signaturemaker.app.application.features.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun MainRoute(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    MainScreen(
        modifier = modifier,
        navController = navController
    )
}

