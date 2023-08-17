package com.minutesock.wordgame.presentation

sealed interface DailyWordEventGame {
    data class OnCharacterPress(val character: Char) : DailyWordEventGame
    object OnEnterPress : DailyWordEventGame
    object OnDeletePress : DailyWordEventGame
    object OnErrorAnimationFinished : DailyWordEventGame
    object OnAnsweredWordRowAnimationFinishedGame : DailyWordEventGame

    object OnCompleteAnimationFinishedGame : DailyWordEventGame

    object OnStatsPress : DailyWordEventGame
}