package com.minutesock.wordgame.presentation

import com.minutesock.wordgame.domain.GuessLetter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DailyWordState(
    val wordLength: Int = 5,
    val maxGuessAttempts: Int = 5,
    val currentGuess: ImmutableList<GuessLetter> = persistentListOf(),
    val previousGuesses: ImmutableList<GuessWord> = persistentListOf(),
    val correctWord: String? = null,
    val message: String = ""
)

data class GuessWord(
    val letters: ImmutableList<GuessLetter>
)
