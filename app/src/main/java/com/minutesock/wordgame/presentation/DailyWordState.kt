package com.minutesock.wordgame.presentation

import com.minutesock.wordgame.domain.GuessKey
import com.minutesock.wordgame.uiutils.UiText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DailyWordState(
    val gameState: DailyWordGameState = DailyWordGameState.NotStarted,
    val wordLength: Int = 5,
    val maxGuessAttempts: Int = 5,
    val correctWord: String? = null,
    val dailyWordStateMessage: DailyWordStateMessage? = null,
    val falseKeyboardKeys: ImmutableList<GuessKey> = persistentListOf()
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



