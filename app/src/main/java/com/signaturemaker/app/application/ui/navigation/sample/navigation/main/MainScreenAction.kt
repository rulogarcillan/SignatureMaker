package com.kubit.charts.storybook.ui.screens.main

/**
 * Navigation actions from MainScreen
 */
sealed interface MainScreenAction {
    data object NavigateToComponentList : MainScreenAction
    data object NavigateToSamples : MainScreenAction
}
