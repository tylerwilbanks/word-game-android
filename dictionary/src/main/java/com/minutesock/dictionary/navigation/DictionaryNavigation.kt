package com.minutesock.dictionary.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.minutesock.core.navigation.BottomNavItem
import com.minutesock.dictionary.presentation.DictionaryScreen

val BASE_ROUTE = BottomNavItem.Dictionary.route

fun NavGraphBuilder.dictionaryRoute(
    modifier: Modifier = Modifier
) {
    composable(route = BASE_ROUTE) {
        DictionaryScreen(
            modifier = modifier
        )
    }
}