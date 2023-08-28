package com.minutesock.wordgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.minutesock.wordgame.domain.BottomNavItem
import com.minutesock.wordgame.domain.GuessWordValidator
import com.minutesock.wordgame.presentation.BottomNavigation
import com.minutesock.wordgame.presentation.DailyWordScreen
import com.minutesock.wordgame.presentation.DailyWordViewModel
import com.minutesock.wordgame.ui.theme.WordGameTheme
import kotlinx.collections.immutable.persistentListOf


class MainActivity : ComponentActivity() {

    private val viewModel: DailyWordViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GuessWordValidator.initValidWords(this)
        viewModel.setupGame()
        val bottomNavItems = persistentListOf(
            BottomNavItem.Daily,
            BottomNavItem.Dictionary,
            BottomNavItem.Profile
        )
        setContent {
            WordGameTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()
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
                                    state = state,
                                    userGuessWords = viewModel.userGuessWords,
                                    onGameEvent = viewModel::onGameEvent,
                                    onStatsEvent = viewModel::onStatsEvent
                                )
                            }
                            composable(BottomNavItem.Dictionary.route) {
                                Text(text = "Dictionary screen is under construction. \uD83D\uDEA7")
                            }
                            composable(BottomNavItem.Profile.route) {
                                Text(text = "Profile screen is under construction. \uD83D\uDEA7")
                            }
                        }
                    }
                )
            }
        }
    }
}



