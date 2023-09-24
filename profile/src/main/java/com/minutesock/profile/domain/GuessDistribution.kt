package com.minutesock.profile.domain

import com.minutesock.core.domain.WordGameState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class GuessDistribution(
    val correctAttempt: Int,
    val correctAttemptCount: Int = 0,
    val gameState: WordGameState,
    val maxGuessAttempts: Int,
    val isMostRecentGame: Boolean = false
)

data class GuessDistributionState(
    val loading: Boolean = false,
    val guessDistributions: ImmutableList<GuessDistribution> = persistentListOf(),
    val failedGameSessionsCount: Int = 0,
    val maxCorrectAttemptCount: Int = 0
)
