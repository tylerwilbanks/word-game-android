package com.minutesock.wordgame.presentation

import com.minutesock.wordgame.uiutils.UiText

data class DailyWordState(
    val gameState: DailyWordGameState = DailyWordGameState.NotStarted,
    val screenState: DailyWordScreenState = DailyWordScreenState.NotStarted,
    val wordLength: Int = 5,
    val maxGuessAttempts: Int = 5,
    val correctWord: String? = null,
    val dailyWordStateMessage: DailyWordStateMessage? = null,
    val falseKeyboardKeys: FalseKeyboardKeys = FalseKeyboardKeys()
)

data class DailyWordStateMessage(
    val uiText: UiText? = null,
    val isError: Boolean = false,
)

enum class DailyWordGameState {
    NotStarted,
    InProgress,
    Success,
    Failure;

    val isGameOver
        get() = when (this) {
            Success -> true
            Failure -> true
            else -> false
        }
}

enum class DailyWordScreenState {
    NotStarted,
    Complete
}



