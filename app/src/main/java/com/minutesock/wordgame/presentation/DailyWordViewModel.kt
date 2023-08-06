package com.minutesock.wordgame.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.minutesock.wordgame.domain.WordGuess

class DailyWordViewModel: ViewModel() {

    var guesses: List<WordGuess> by mutableStateOf(
        List(5) {
            WordGuess(it)
        })
        private set

    var currentGuess by mutableStateOf(
        guesses.first {!it.lockedIn}
    )

    fun onEvent(event: DailyWordEvent) {
        // todo implement
    }
}