package com.minutesock.infinity.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.minutesock.core.navigation.BottomNavItem
import com.minutesock.core.presentation.HowToPlayScreen
import com.minutesock.infinity.presentation.InfinityScreen

val BASE_ROUTE = BottomNavItem.Infinity.route

fun NavGraphBuilder.infinityRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    isDarkTheme: Boolean,
    onDarkThemeToggled: (Boolean) -> Unit,
) {
    composable(route = BASE_ROUTE) {
        InfinityScreen(
            modifier = modifier,
            navController = navController,
            isDarkTheme = isDarkTheme,
            onDarkThemeToggled = onDarkThemeToggled,
        )
    }

    composable(route = "how") {
        HowToPlayScreen(
            modifier = modifier,
            navController = navController
        )
    }
}