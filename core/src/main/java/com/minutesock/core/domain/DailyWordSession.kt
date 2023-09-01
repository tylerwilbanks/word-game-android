package com.minutesock.core.domain

import kotlinx.collections.immutable.ImmutableList
import java.util.Date

data class DailyWordSession(
    val id: Int = 0,
    val date: Date,
    val correctWord: String,
    val maxAttempts: Int,
    val guesses: ImmutableList<GuessWord>,
    val isDaily: Boolean,
    val gameState: DailyWordGameState
) {
    val wordLength = correctWord.length
}