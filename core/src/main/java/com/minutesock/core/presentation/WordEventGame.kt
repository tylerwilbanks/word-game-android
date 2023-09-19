package com.minutesock.core.presentation

sealed interface WordEventGame {
    data class OnCharacterPress(val character: Char) : WordEventGame
    object OnEnterPress : WordEventGame
    object OnDeletePress : WordEventGame
    object OnErrorAnimationFinished : WordEventGame
    object OnAnsweredWordRowAnimationFinished : WordEventGame

    object OnCompleteAnimationFinished : WordEventGame

    object OnStatsPress : WordEventGame
}