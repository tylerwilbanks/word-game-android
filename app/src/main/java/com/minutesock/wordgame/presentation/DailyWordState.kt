package com.minutesock.wordgame.presentation

import com.minutesock.wordgame.uiutils.UiText

data class DailyWordState(
    val gameState: DailyWordGameState = DailyWordGameState.NotStarted,
    val wordLength: Int = 5,
    val maxGuessAttempts: Int = 5,
    val correctWord: String? = null,
    val dailyWordStateMessage: DailyWordStateMessage? = null
)

data class DailyWordStateMessage(
    val uiText: UiText? = null,
    val isError: Boolean = false,
)

enum class DailyWordGameState{
    NotStarted,
    InProgress,
    Complete
}



