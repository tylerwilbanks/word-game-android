package com.minutesock.wordgame.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.wordgame.domain.GuessWord
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DailyWordViewModel : ViewModel() {

    private val _state = MutableStateFlow(DailyWordState())
    val state
        get() = _state.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            DailyWordState()
        )

    private val errorMessageDelay = 1000L

    fun setupGame() {
        val guesses = List(5) {
            GuessWord(it)
        }
        _state.value = DailyWordState(
            guesses = guesses,
            currentGuess = guesses.first { !it.lockedIn },
            chosenWord = "Jumby"
        )
    }

    fun onEvent(event: DailyWordEvent) {
        when (event) {
            is DailyWordEvent.OnCharacterPress -> {
                viewModelScope.launch {
                    val currentGuess = _state.value.currentGuess
                    currentGuess?.getLetterForInput?.let { guessLetter ->
                        guessLetter.updateCharacter(event.character)
                        _state.update {
                            it.copy(
                                currentGuess = currentGuess
                            )
                        }
                    }

                    updateMessage("You pressed ${event.character}")
                }
            }

            DailyWordEvent.OnDeletePress -> {
                viewModelScope.launch {
                    val currentGuess = _state.value.currentGuess
                    currentGuess?.getLetterToErase?.let { guessLetter ->
                        guessLetter.updateCharacter(' ')
                        _state.update {
                            it.copy(
                                currentGuess = currentGuess
                            )
                        }
                    }
                    updateMessage("You pressed delete!")
                }
            }

            DailyWordEvent.OnEnterPress -> {
                viewModelScope.launch {
                    updateMessage("You pressed enter!")
                }
            }
        }
    }

    private suspend fun updateMessage(message: String) {
        _state.update {
            it.copy(message = message)
        }
        delay(errorMessageDelay)
        _state.update {
            it.copy(message = null)
        }
    }
}