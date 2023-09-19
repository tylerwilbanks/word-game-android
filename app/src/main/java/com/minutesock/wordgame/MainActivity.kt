package com.minutesock.wordgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.minutesock.core.domain.GuessWordValidator
import com.minutesock.core.navigation.BottomNavItem
import com.minutesock.core.theme.WordGameTheme
import com.minutesock.daily.navigation.dailyRoute
import com.minutesock.infinity.navigation.infinityRoute
import com.minutesock.profile.navigation.navigateToHistory
import com.minutesock.profile.navigation.profileRoute
import com.minutesock.wordgame.presentation.BottomNavigation
import kotlinx.collections.immutable.persistentListOf


class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GuessWordValidator.initValidWords(this)
        val bottomNavItems = persistentListOf(
            BottomNavItem.Daily,
            BottomNavItem.Infinity,
            BottomNavItem.Dictionary,
            BottomNavItem.Profile
        )
        setContent {
            WordGameTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigation(
                            navController = navController,
                            bottomNavItems = bottomNavItems
                        )
                    },
                    content = { paddingValues ->
                        NavHost(navController, startDestination = BottomNavItem.Daily.route) {
                            val bottomModifier =
                                Modifier.padding(bottom = paddingValues.calculateBottomPadding())
                            dailyRoute(modifier = bottomModifier)
                            infinityRoute(modifier = bottomModifier)
                            composable(BottomNavItem.Dictionary.route) {
                                Column(
                                    modifier = bottomModifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        textAlign = TextAlign.Center,
                                        text = "Dictionary screen is under construction. \uD83D\uDEA7"
                                    )
                                }
                            }
                            profileRoute(
                                modifier = bottomModifier,
                                onHistoryButtonClicked = navController::navigateToHistory,
                                onBackButtonClicked = navController::popBackStack
                            )
                        }
                    }
                )
            }
        }
    }
}



