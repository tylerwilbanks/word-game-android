package com.minutesock.wordgame.presentation

data class DailyWordState(
    val wordLength: Int = 5,
    val maxGuessAttempts: Int = 5,
    val correctWord: String? = null,
    val dailyWordStateMessage: DailyWordStateMessage? = null
)

data class DailyWordStateMessage(
    val message: String = "",
    val isError: Boolean = false,
)



