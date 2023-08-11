package com.minutesock.wordgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.minutesock.wordgame.domain.GuessWordValidator
import com.minutesock.wordgame.presentation.DailyWordScreen
import com.minutesock.wordgame.presentation.DailyWordViewModel
import com.minutesock.wordgame.presentation.FalseKeyboardKeys
import com.minutesock.wordgame.ui.theme.WordGameTheme


class MainActivity : ComponentActivity() {

    private val viewModel: DailyWordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GuessWordValidator.initValidWords(this)
        viewModel.setupGame()
        setContent {
            WordGameTheme {
                DailyWordScreen(
                    state = viewModel.state.collectAsStateWithLifecycle(),
                    onEvent = viewModel::onEvent,
                    falseKeyboardKeys = FalseKeyboardKeys()
                )
            }
        }
    }
}



