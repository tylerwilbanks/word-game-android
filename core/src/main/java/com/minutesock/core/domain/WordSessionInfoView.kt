package com.minutesock.core.domain

import kotlinx.collections.immutable.ImmutableList

data class WordSessionInfoView(
    val displayDate: String,
    val guessWordRowInfoViews: ImmutableList<GuessWordRowInfoView>,
    val displayCompleteTime: String,
    val gameMode: WordGameMode,
    val gameState: WordGameState
)
