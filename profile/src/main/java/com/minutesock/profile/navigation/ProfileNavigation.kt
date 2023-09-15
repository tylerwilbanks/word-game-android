package com.minutesock.profile.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.minutesock.core.navigation.BottomNavItem
import com.minutesock.profile.presentation.ProfileScreen

fun NavGraphBuilder.profileScreen(modifier: Modifier = Modifier) {
    composable(route = BottomNavItem.Profile.route) {
        ProfileScreen(modifier = modifier)
    }
}