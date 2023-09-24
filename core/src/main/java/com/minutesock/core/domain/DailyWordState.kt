package com.minutesock.core.domain

import com.minutesock.core.presentation.FalseKeyboardKeys
import com.minutesock.core.uiutils.UiText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DailyWordState(
    val wordRowAnimating: Boolean = false,
    val gameState: WordGameState = WordGameState.NotStarted,
    val screenState: WordScreenState = WordScreenState.NotStarted,
    val wordLength: Int = 5,
    val maxGuessAttempts: Int = 6,
    val correctWord: String? = null,
    val dailyWordStateMessage: DailyWordStateMessage? = null,
    val falseKeyboardKeys: FalseKeyboardKeys = FalseKeyboardKeys(),
    val shareText: String? = null,
    val definitionMessage: String? = null,
    val wordInfos: ImmutableList<WordInfo> = persistentListOf(),
    val guessWords: ImmutableList<GuessWord> = persistentListOf(),
    val gameMode: WordGameMode = WordGameMode.Daily
)

data class DailyWordStateMessage(
    val uiText: UiText? = null,
    val isError: Boolean = false,
)

enum class WordGameState {
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
        fun fromInt(ordinal: Int) = WordGameState.values().first { it.ordinal == ordinal }
    }
}

enum class WordScreenState {
    NotStarted,
    Game,
    Stats
}

enum class WordGameMode {
    Daily,
    Inifinity
}


