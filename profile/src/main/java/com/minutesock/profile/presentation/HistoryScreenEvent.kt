package com.minutesock.profile.presentation

sealed class HistoryScreenEvent {
    data class UpdateScrollPosition(val scrollPosition: Int) : HistoryScreenEvent()
}
