package com.minutesock.daily.presentation

sealed interface DailyWordEventStats {
    object OnExitButtonPressed : DailyWordEventStats
    object OnShareButtonPressed : DailyWordEventStats
    object OnShareChooserPresented : DailyWordEventStats
}