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
import com.signaturemaker.app.application.features.main.MainMenuItem.Sign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/*
 * State holder for Main Screen following Compose best practices
 * All state is properly hoisted and observable
 */
@Stable
data class MainUIState(
    val menuSelected: MainMenuItem,
    val drawerState: DrawerState,
    val onDrawerClick: () -> Unit,
    val changeMenuSelected: (MainMenuItem) -> Unit
)

/*
 * Remember MainUIState following Compose best practices
 * The state holder is remembered and its internal state is properly observable
 */
@Composable
fun rememberMainState(
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
): MainUIState {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    var selectedMenu by remember { mutableStateOf<MainMenuItem>(Sign) }

    val changeMenuSelected = { it: MainMenuItem -> selectedMenu = it }

    fun onDrawerClick() {
        coroutineScope.launch {
            when (drawerState.currentValue) {
                DrawerValue.Closed -> drawerState.open()
                else -> drawerState.close()
            }
        }
    }

    return remember(selectedMenu, drawerState, coroutineScope) {
        MainUIState(
            menuSelected = selectedMenu,
            drawerState = drawerState,
            onDrawerClick = ::onDrawerClick,
            changeMenuSelected = changeMenuSelected
        )
    }
}
