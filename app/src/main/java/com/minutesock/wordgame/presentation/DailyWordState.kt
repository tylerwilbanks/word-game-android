package com.minutesock.wordgame.presentation

import androidx.compose.runtime.Immutable
import com.minutesock.wordgame.domain.GuessLetter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class DailyWordState(
    val wordLength: Int = 5,
    val maxGuessAttempts: Int = 5,
    val currentGuess: ImmutableList<GuessLetter> = persistentListOf(),
    val previousGuesses: ImmutableList<GuessWord> = persistentListOf(),
    val correctWord: String? = null,
    val message: String? = null
)

data class GuessWord(
    val letters: ImmutableList<GuessLetter>
)
