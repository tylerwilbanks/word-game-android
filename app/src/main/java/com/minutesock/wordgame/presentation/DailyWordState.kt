package com.minutesock.wordgame.presentation

import com.minutesock.wordgame.domain.WordInfo
import com.minutesock.wordgame.uiutils.UiText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DailyWordState(
    val wordRowAnimating: Boolean = false,
    val gameState: DailyWordGameState = DailyWordGameState.NotStarted,
    val screenState: DailyWordScreenState = DailyWordScreenState.NotStarted,
    val wordLength: Int = 5,
    val maxGuessAttempts: Int = 6,
    val correctWord: String? = null,
    val dailyWordStateMessage: DailyWordStateMessage? = null,
    val falseKeyboardKeys: FalseKeyboardKeys = FalseKeyboardKeys(),
    val shareText: String? = null,
    val definitionMessage: String? = null,
    val wordInfo: ImmutableList<WordInfo> = persistentListOf()
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
    Game,
    Stats
}



