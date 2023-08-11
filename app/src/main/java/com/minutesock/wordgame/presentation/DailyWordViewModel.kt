package com.minutesock.wordgame.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.minutesock.wordgame.domain.GuessLetter
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
                correctWord = "wrath"
            )
        }
    }

    fun onEvent(event: DailyWordEvent) {
        when (event) {
            is DailyWordEvent.OnCharacterPress -> {
                if (state.value.currentGuess.size >= state.value.wordLength) {
                    return
                }
                _state.value = state.value.copy(
                    currentGuess = state.value.currentGuess.plus(GuessLetter(event.character))
                        .toImmutableList()
                )
            }

            DailyWordEvent.OnDeletePress -> {
                if (state.value.currentGuess.isEmpty()) {
                    return
                }
                _state.value = state.value.copy(
                    currentGuess = state.value.currentGuess.subList(
                        0,
                        state.value.currentGuess.size - 1
                    )
                        .toImmutableList()
                )
            }

            DailyWordEvent.OnEnterPress -> {
                val lastWord = state.value.currentGuess.takeLast(
                    state.value.wordLength
                )
                state
//                _state.value = state.value.copy(
//                    currentGuess = state.value.currentGuess.plus(Gu)
//                )
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