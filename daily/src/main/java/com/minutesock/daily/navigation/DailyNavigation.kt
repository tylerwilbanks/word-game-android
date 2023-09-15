package com.minutesock.daily.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.minutesock.core.navigation.BottomNavItem
import com.minutesock.daily.presentation.DailyWordScreen

fun NavGraphBuilder.dailWordScreen(modifier: Modifier = Modifier) {
    composable(route = BottomNavItem.Daily.route) {
        DailyWordScreen(modifier = modifier)
    }
}