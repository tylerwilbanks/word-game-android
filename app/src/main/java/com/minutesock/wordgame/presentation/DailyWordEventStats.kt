package com.minutesock.wordgame.presentation

sealed interface DailyWordEventStats {
    object OnExitButtonPressed : DailyWordEventStats
    object OnShareButtonPressed : DailyWordEventStats
    object OnShareChooserPresented : DailyWordEventStats
}