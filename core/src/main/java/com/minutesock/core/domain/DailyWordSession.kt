package com.minutesock.core.domain

import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.Instant
import java.util.Date

data class DailyWordSession(
    val id: Int = 0,
    val date: Date,
    val correctWord: String,
    val maxAttempts: Int,
    val guesses: ImmutableList<GuessWord>,
    val isDaily: Boolean,
    val gameState: DailyWordGameState,
    val startTime: Instant? = null
) {
    val wordLength = correctWord.length

    val completeTime = guesses.lastOrNull { it.completeTime != null }

    val successTime = guesses.lastOrNull { it.completeTime != null && it.state == GuessWordState.Correct }
}