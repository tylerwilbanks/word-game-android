package com.minutesock.wordgame.presentation

import com.minutesock.wordgame.domain.GuessLetter

data class DailyWordState(
    val wordLength: Int = 5,
    val maxGuessAttempts: Int = 5,
    val letters: List<GuessLetter> = emptyList(),
    val currentGuess: List<GuessLetter> = emptyList(),
    val correctWord: String? = null,
    val message: String? = null
)
