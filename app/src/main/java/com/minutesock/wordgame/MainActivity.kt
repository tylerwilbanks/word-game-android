package com.minutesock.wordgame

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.minutesock.core.domain.GuessWordValidator
import com.minutesock.core.navigation.BottomNavItem
import com.minutesock.core.theme.WordGameTheme
import com.minutesock.daily.navigation.dailyRoute
import com.minutesock.dictionary.navigation.dictionaryRoute
import com.minutesock.infinity.navigation.infinityRoute
import com.minutesock.profile.navigation.navigateToHistory
import com.minutesock.profile.navigation.profileRoute
import com.minutesock.wordgame.presentation.AppViewModel
import com.minutesock.wordgame.presentation.BottomNavigation
import kotlinx.collections.immutable.persistentListOf


class MainActivity : ComponentActivity() {

    private val viewModel: AppViewModel by viewModels()


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
        val isDarkMode =
            when (this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> true
                Configuration.UI_MODE_NIGHT_NO -> false
                else -> false
            }
        viewModel.toggleDarkMode(
            toggle = isDarkMode
        )
        setContent {
            val isDarkTheme by viewModel.isDarkMode.collectAsStateWithLifecycle()
            WordGameTheme(
                useDarkTheme = isDarkTheme
            ) {
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
                            dictionaryRoute(
                                modifier = bottomModifier,
                                navController = navController,
                            )
                            profileRoute(
                                modifier = bottomModifier,
                                onHistoryButtonClicked = navController::navigateToHistory,
                                onBackButtonClicked = navController::popBackStack,
                                isDarkTheme = isDarkTheme,
                                onDarkThemeToggled = viewModel::toggleDarkMode,
                            )
                        }
                    }
                )
            }
        }
    }
}



