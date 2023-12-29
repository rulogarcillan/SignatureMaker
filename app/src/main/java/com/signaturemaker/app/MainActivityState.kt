package com.signaturemaker.app

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
class MainActivityState(
    val navController: NavHostController,
    val menuSelected: MainActivityMenu,
    val drawerState: DrawerState,
    val onDrawerClick: () -> Unit,
    val changeMenuSelected: (MainActivityMenu) -> Unit
) {
    fun navigateTo(destination: String) {
        navController.navigate(destination) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }

}

@Composable
fun rememberMainActivityState(
    navController: NavHostController,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): MainActivityState {

    var selectedMenu by remember { mutableStateOf<MainActivityMenu>(MainActivityMenu.Sign) }

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

