package com.minutesock.profile.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.minutesock.core.navigation.BottomNavItem
import com.minutesock.profile.presentation.HistoryScreen
import com.minutesock.profile.presentation.ProfileScreen

val BASE_ROUTE = BottomNavItem.Profile.route

val HISTORY_ROUTE = "${BASE_ROUTE}/history"

fun NavGraphBuilder.profileScreen(modifier: Modifier = Modifier) {
    composable(route = BASE_ROUTE) {
        ProfileScreen(modifier = modifier)
    }

    composable(route = HISTORY_ROUTE) {
        HistoryScreen()
    }
}