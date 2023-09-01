package com.minutesock.core.domain

import com.minutesock.core.presentation.FalseKeyboardKeys
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
    val wordInfos: ImmutableList<WordInfo> = persistentListOf()
)

data class DailyWordStateMessage(
    val uiText: com.minutesock.core.uiutils.UiText? = null,
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

    companion object {
        fun fromInt(ordinal: Int) = DailyWordGameState.values().first { it.ordinal == ordinal }
    }
}

enum class DailyWordScreenState {
    NotStarted,
    Game,
    Stats
}



