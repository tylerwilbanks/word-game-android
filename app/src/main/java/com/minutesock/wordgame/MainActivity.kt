package com.minutesock.wordgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.minutesock.daily.domain.GuessWordValidator
import com.minutesock.daily.presentation.DailyWordScreen
import com.minutesock.profile.presentation.ProfileScreen
import com.minutesock.wordgame.domain.BottomNavItem
import com.minutesock.wordgame.presentation.BottomNavigation
import com.minutesock.wordgame.theme.WordGameTheme
import kotlinx.collections.immutable.persistentListOf


class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GuessWordValidator.initValidWords(this)
        val bottomNavItems = persistentListOf(
            BottomNavItem.Daily,
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
                            composable(BottomNavItem.Daily.route) {
                                DailyWordScreen(
                                    modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                                )
                            }
                            composable(BottomNavItem.Dictionary.route) {
                                Text(text = "Dictionary screen is under construction. \uD83D\uDEA7")
                            }
                            composable(BottomNavItem.Profile.route) {
                                ProfileScreen()
                            }
                        }
                    }
                )
            }
        }
    }
}



