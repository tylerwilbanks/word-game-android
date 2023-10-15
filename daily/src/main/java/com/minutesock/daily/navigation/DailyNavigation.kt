package com.minutesock.daily.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.minutesock.core.navigation.BottomNavItem
import com.minutesock.core.presentation.HowToPlayScreen
import com.minutesock.daily.presentation.DailyWordGameScreen

val BASE_ROUTE = BottomNavItem.Daily.route

fun NavGraphBuilder.dailyRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    isDarkTheme: Boolean,
    onDarkThemeToggled: (Boolean) -> Unit,
) {
    composable(route = BASE_ROUTE) {
        DailyWordGameScreen(
            modifier = modifier,
            navController = navController,
            isDarkTheme = isDarkTheme,
            onDarkThemeToggled = onDarkThemeToggled
        )
    }

    composable(route = "how") {
        HowToPlayScreen(
            modifier = modifier,
            navController = navController,
        )
    }
}