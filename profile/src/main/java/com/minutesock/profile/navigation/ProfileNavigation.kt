package com.minutesock.profile.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.minutesock.core.navigation.BottomNavItem
import com.minutesock.profile.presentation.HistoryRoute
import com.minutesock.profile.presentation.ProfileScreen

val BASE_ROUTE = BottomNavItem.Profile.route
val HISTORY_ROUTE = "${BASE_ROUTE}/history"

fun NavController.navigateToHistory() {
    this.navigate(HISTORY_ROUTE)
}

fun NavGraphBuilder.profileRoute(
    onHistoryButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit,
    isDarkTheme: Boolean,
    onDarkThemeToggled: (Boolean) -> Unit,
) {
    composable(route = BASE_ROUTE) {
        ProfileScreen(
            onHistoryButtonClicked = onHistoryButtonClicked,
            modifier = modifier,
            isDarkTheme = isDarkTheme,
            onDarkThemeToggled = onDarkThemeToggled,
        )
    }

    composable(route = HISTORY_ROUTE) {
        HistoryRoute(onBackButtonClicked = onBackButtonClicked)
    }
}