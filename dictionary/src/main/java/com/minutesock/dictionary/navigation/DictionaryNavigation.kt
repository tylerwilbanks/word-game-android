package com.minutesock.dictionary.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.minutesock.core.navigation.BottomNavItem
import com.minutesock.dictionary.presentation.DictionaryDetail
import com.minutesock.dictionary.presentation.DictionaryScreen

val BASE_ROUTE = BottomNavItem.Dictionary.route
val DETAIL_ROUTE = "$BASE_ROUTE/{word}"

fun NavGraphBuilder.dictionaryRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    composable(route = BASE_ROUTE) {
        DictionaryScreen(
            modifier = modifier,
            navController = navController
        )
    }
    composable(
        route = DETAIL_ROUTE,
        arguments = listOf(navArgument("word") { type = NavType.StringType })
        ) {
        DictionaryDetail(
            word = it.arguments?.getString("word")!!,
            navController = navController
            )
    }
}

internal fun NavController.navigateToDictionaryDetail(word: String) {
    navigate("$BASE_ROUTE/$word")
}