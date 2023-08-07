package com.minutesock.wordgame.presentation

import com.minutesock.wordgame.domain.WordGuess

data class DailyWordState(
    val guesses: List<WordGuess> = emptyList(),
    val currentGuess: WordGuess? = null,
    val message: String? = null
)
