package com.minutesock.daily.presentation

sealed interface DailyWordEventGame {
    data class OnCharacterPress(val character: Char) : DailyWordEventGame
    object OnEnterPress : DailyWordEventGame
    object OnDeletePress : DailyWordEventGame
    object OnErrorAnimationFinished : DailyWordEventGame
    object OnAnsweredWordRowAnimationFinished : DailyWordEventGame

    object OnCompleteAnimationFinished : DailyWordEventGame

    object OnStatsPress : DailyWordEventGame
}