package com.minutesock.infinity.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.minutesock.core.navigation.BottomNavItem
import com.minutesock.infinity.presentation.InfinityScreen

val BASE_ROUTE = BottomNavItem.Infinity.route

fun NavGraphBuilder.infinityRoute(
    modifier: Modifier = Modifier
) {
    composable(route = BASE_ROUTE) {
        InfinityScreen(modifier = modifier)
    }
}