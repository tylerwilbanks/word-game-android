package com.minutesock.profile.domain

import com.minutesock.core.domain.DailyWordGameState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class GuessDistribution(
    val correctAttempt: Int,
    val correctAttemptCount: Int = 0,
    val gameState: DailyWordGameState,
    val maxGuessAttempts: Int,
)

data class GuessDistributionState(
    val loading: Boolean = false,
    val guessDistributions: ImmutableList<GuessDistribution> = persistentListOf(),
    val failedGameSessionsCount: Int = 0,
    val maxCorrectAttemptCount: Int = 0
)
