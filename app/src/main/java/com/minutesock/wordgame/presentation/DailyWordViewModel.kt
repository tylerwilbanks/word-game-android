package com.minutesock.wordgame.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.wordgame.domain.WordGuess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class DailyWordViewModel: ViewModel() {

    private val _state = MutableStateFlow(DailyWordState())
    val state get() = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), DailyWordState())

    fun setupGame() {
        val guesses = List(5) {
            WordGuess(it)
        }
        _state.value = DailyWordState(
            guesses = guesses,
            currentGuess = guesses.first {!it.lockedIn},
        )
    }

    fun onEvent(event: DailyWordEvent) {
        // todo implement
    }
}