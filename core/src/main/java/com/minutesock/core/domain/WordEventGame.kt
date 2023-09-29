package com.minutesock.core.domain

sealed interface WordEventGame {
    data class OnCharacterPress(val character: Char) : WordEventGame
    data object OnEnterPress : WordEventGame
    data object OnDeletePress : WordEventGame
    data object OnErrorAnimationFinished : WordEventGame
    data object OnAnsweredWordRowAnimationFinished : WordEventGame

    data object OnCompleteAnimationFinished : WordEventGame

    data object OnStatsPress : WordEventGame
}