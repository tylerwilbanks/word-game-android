package com.minutesock.wordgame.presentation

import com.minutesock.wordgame.domain.GuessWord

data class DailyWordState(
    val guesses: List<GuessWord> = emptyList(),
    val currentGuess: GuessWord? = null,
    val currentWord: String? = null,
    val correctWord: String? = null,
    val message: String? = null
)
