package com.signaturemaker.app.application.features.main

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.signaturemaker.app.application.features.main.MainActivityMenu.Sign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Main Activity Menu
 */
@Stable
data class MainActivityState(
    val navController: NavHostController,
    val menuSelected: MainActivityMenu,
    val drawerState: DrawerState,
    val onDrawerClick: () -> Unit,
    val changeMenuSelected: (MainActivityMenu) -> Unit
) {
    /**
     * Navigate to a destination and clear back stack
     */
    fun navigateTo(destination: String) {
        navController.navigate(destination) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }
}

/**
 * Remember MainActivityState
 */
@Composable
fun rememberMainActivityState(
    navController: NavHostController,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): MainActivityState {
    var selectedMenu by remember { mutableStateOf<MainActivityMenu>(Sign) }

    val changeMenuSelected = { it: MainActivityMenu -> selectedMenu = it }

    fun onDrawerClick() {
        coroutineScope.launch {
            when (drawerState.currentValue) {
                DrawerValue.Closed -> drawerState.open()
                else -> drawerState.close()
            }
        }
    }

    return remember(navController, selectedMenu, drawerState, coroutineScope) {
        MainActivityState(
            navController = navController,
            menuSelected = selectedMenu,
            drawerState = drawerState,
            onDrawerClick = ::onDrawerClick,
            changeMenuSelected = changeMenuSelected
        )
    }
}
