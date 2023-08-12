package com.minutesock.wordgame.presentation

data class DailyWordState(
    val wordLength: Int = 5,
    val maxGuessAttempts: Int = 5,
    val correctWord: String? = null,
    val message: String = ""
)



