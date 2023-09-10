package com.minutesock.profile.domain

import com.minutesock.core.domain.DailyWordGameState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class GuessDistribution(
    val correctAttempt: Int? = null,
    val gameState: DailyWordGameState,
    val maxGuessAttempts: Int,
)

data class GuessDistributionState(
    val loading: Boolean = false,
    val guessDistributions: ImmutableList<ImmutableList<GuessDistribution>> = persistentListOf(),
    val failedGameSessionsCount: Int = 0
)
