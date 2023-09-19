package com.minutesock.daily.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.minutesock.core.navigation.BottomNavItem
import com.minutesock.daily.presentation.DailyWordScreen

val BASE_ROUTE = BottomNavItem.Daily.route

fun NavGraphBuilder.dailyRoute(modifier: Modifier = Modifier) {
    composable(route = BASE_ROUTE) {
        DailyWordScreen(modifier = modifier)
    }
}