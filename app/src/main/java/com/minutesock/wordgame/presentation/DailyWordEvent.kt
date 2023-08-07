package com.minutesock.wordgame.presentation

sealed interface DailyWordEvent {
    data class OnCharacterPress(val character: Char): DailyWordEvent
    object OnEnterPress: DailyWordEvent
    object OnDeletePress: DailyWordEvent
}