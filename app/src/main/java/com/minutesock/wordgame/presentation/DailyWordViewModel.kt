package com.minutesock.wordgame.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.wordgame.domain.DailyWordValidationResultType
import com.minutesock.wordgame.domain.GuessWord
import com.minutesock.wordgame.domain.GuessWordValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DailyWordViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(DailyWordState())
    private val context = getApplication<Application>().applicationContext

    val state = _state.asStateFlow()

    private val errorMessageDelay = 1000L

    fun setupGame() {
        val guesses = List(5) {
            GuessWord()
        }

        _state.update { dailyWordState ->
            dailyWordState.copy(
                guesses = guesses,
                currentGuess = guesses.firstOrNull { !it.lockedIn },
                correctWord = "wrath"
            )
        }
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
                                currentGuess = currentGuess,
                                currentWord = currentGuess.displayWord
                            )
                        }
                    }
                }
            }

            DailyWordEvent.OnDeletePress -> {
                viewModelScope.launch {
                    val currentGuess = _state.value.currentGuess
                    currentGuess?.getLetterToErase?.let { guessLetter ->
                        guessLetter.updateCharacter(' ')
                        _state.update {
                            it.copy(
                                currentGuess = currentGuess,
                                currentWord = currentGuess.displayWord
                            )
                        }
                    }
                }
            }

            DailyWordEvent.OnEnterPress -> {
                viewModelScope.launch {
                    _state.value.currentGuess?.let { currentGuess ->
                        _state.value.correctWord?.let { correctWord ->
                            val result = GuessWordValidator.validateGuess(
                                context,
                                currentGuess,
                                correctWord
                            )
                            when (result.type) {
                                DailyWordValidationResultType.Error -> {}
                                DailyWordValidationResultType.Incorrect -> currentGuess.updateGuess(
                                    correctWord = correctWord
                                )

                                DailyWordValidationResultType.Success -> currentGuess.updateGuess(
                                    correctWord = correctWord
                                )
                            }
                            _state.update { dailyWordState ->
                                dailyWordState.copy(
                                    message = result.message,
                                    currentGuess = _state.value.guesses.firstOrNull { !it.lockedIn }
                                )
                            }
                        }
                    }
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