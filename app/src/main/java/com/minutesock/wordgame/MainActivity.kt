package com.minutesock.wordgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.minutesock.wordgame.domain.WordGuess
import com.minutesock.wordgame.domain.WordGuessValidator
import com.minutesock.wordgame.presentation.DailyWordScreen
import com.minutesock.wordgame.ui.theme.WordGameTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordGameTheme {
                DailyWordScreen()
            }
        }
        WordGuessValidator.initValidWords(this)
        WordGuessValidator.validateGuess(this, WordGuess(), "Jumby")
    }
}



