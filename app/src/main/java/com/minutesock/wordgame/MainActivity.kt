package com.minutesock.wordgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.minutesock.wordgame.domain.WordGuessValidator
import com.minutesock.wordgame.presentation.DailyWordScreen
import com.minutesock.wordgame.presentation.DailyWordViewModel
import com.minutesock.wordgame.ui.theme.WordGameTheme


class MainActivity : ComponentActivity() {

    private val viewModel: DailyWordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WordGuessValidator.initValidWords(this)
        viewModel.setupGame()
        setContent {
            WordGameTheme {
                DailyWordScreen(state = viewModel.state.collectAsState().value, onEvent = viewModel::onEvent)
            }
        }
    }
}



