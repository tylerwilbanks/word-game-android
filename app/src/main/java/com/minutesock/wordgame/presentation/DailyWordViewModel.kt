package com.minutesock.wordgame.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.minutesock.wordgame.domain.DailyWordValidationResultType
import com.minutesock.wordgame.domain.GuessLetter
import com.minutesock.wordgame.domain.GuessWordValidator
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
                _state.value = state.value.copy(
                    letters = state.value.letters.plus(GuessLetter(event.character))
                )
            }

            DailyWordEvent.OnDeletePress -> {
                if (state.value.letters.isEmpty()) {
                    return
                }
                _state.value = state.value.copy(
                    letters = state.value.letters.subList(0, state.value.letters.size - 1)
                )
            }

            DailyWordEvent.OnEnterPress -> {

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