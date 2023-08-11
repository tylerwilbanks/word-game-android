package com.minutesock.wordgame.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.wordgame.R
import com.minutesock.wordgame.domain.GuessLetter
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DailyWordViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(DailyWordState())
    private val context = getApplication<Application>().applicationContext

    val state = _state.asStateFlow()

    private val errorMessageDelay = 1000L

    fun setupGame(wordLength: Int = 5, maxGuessAttempts: Int = 5) {
        _state.update { dailyWordState ->
            dailyWordState.copy(
                wordLength = wordLength,
                maxGuessAttempts = maxGuessAttempts,
                correctWord = "wrath",
                message = context.getString(R.string.what_in_da_word)
            )
        }
    }

    fun onEvent(event: DailyWordEvent) {
        when (event) {
            is DailyWordEvent.OnCharacterPress -> {
                viewModelScope.launch {
                    if (state.value.currentGuess.size >= state.value.wordLength) {
                        return@launch
                    }
                    _state.update {
                        it.copy(
                            currentGuess = state.value.currentGuess.plus(GuessLetter(event.character))
                                .toImmutableList()
                        )
                    }
                }
            }

            DailyWordEvent.OnDeletePress -> {
                viewModelScope.launch {
                    if (state.value.currentGuess.isEmpty()) {
                        return@launch
                    }
                    _state.update {
                        it.copy(
                            currentGuess = state.value.currentGuess.subList(
                                0,
                                state.value.currentGuess.size - 1
                            )
                                .toImmutableList()
                        )
                    }
                }
            }

            DailyWordEvent.OnEnterPress -> {
                viewModelScope.launch {
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
            it.copy(message = "")
        }
    }
}