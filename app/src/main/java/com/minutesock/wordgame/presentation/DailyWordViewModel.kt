package com.minutesock.wordgame.presentation

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.wordgame.R
import com.minutesock.wordgame.domain.GuessLetter
import com.minutesock.wordgame.domain.GuessWord
import com.minutesock.wordgame.domain.GuessWordError
import com.minutesock.wordgame.domain.GuessWordState
import com.minutesock.wordgame.domain.addGuessLetter
import com.minutesock.wordgame.domain.eraseLetter
import com.minutesock.wordgame.domain.updateState
import com.minutesock.wordgame.utils.Resource
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DailyWordViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(DailyWordState())
    val state = _state.asStateFlow()

    private val context = getApplication<Application>().applicationContext

    val guessWords = mutableStateListOf<GuessWord>()

    private val errorMessageDelay = 1000L

    fun setupGame(wordLength: Int = 5, maxGuessAttempts: Int = 5) {
        val w = List(maxGuessAttempts) {
            GuessWord(
                List(wordLength) {
                    GuessLetter()
                }.toImmutableList()
            )
        }.toMutableList()
        w[0] = w[0].updateState(GuessWordState.Editing)
        guessWords.addAll(w)
        _state.update { dailyWordState ->
            dailyWordState.copy(
                wordLength = wordLength,
                maxGuessAttempts = maxGuessAttempts,
                correctWord = "smack",
                message = context.getString(R.string.what_in_da_word)
            )
        }
    }

    fun onEvent(event: DailyWordEvent) {
        when (event) {
            is DailyWordEvent.OnCharacterPress -> {
                viewModelScope.launch {
                    getCurrentGuessWordAndHandleError()?.let { index ->
                        updateCurrentGuessWord(index, event.character)
                    }
                }
            }

            DailyWordEvent.OnDeletePress -> {
                viewModelScope.launch {
                    getCurrentGuessWordAndHandleError()?.let { index ->
                        eraseLetter(index)
                    }
                }
            }

            DailyWordEvent.OnEnterPress -> {
                viewModelScope.launch {

                }
            }

            DailyWordEvent.OnWordRowErrorAnimationFinished -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            message = context.getString(R.string.what_in_da_word)
                        )
                    }
                }
            }
        }
    }

    private fun getCurrentGuessWordIndex(): Resource<Int> {
        val index = guessWords.indexOfFirst {
            it.state == GuessWordState.Editing
        }
        return if (index == -1) {
            Resource.Error("There is no more guess attempts left")
        } else {
            Resource.Success(index)
        }
    }

    private fun getCurrentGuessWordAndHandleError(): Int? {
        return when (val result = getCurrentGuessWordIndex()) {
            is Resource.Error -> {
                result.message?.let { errorMessage ->
                    _state.update {
                        it.copy(
                            message = errorMessage
                        )
                    }
                }
                null
            }

            is Resource.Success -> result.data
        }
    }

    private fun updateCurrentGuessWord(index: Int, character: Char) {
        val result = guessWords[index].addGuessLetter(
            GuessLetter(_character = character)
        )
        when (result) {
            is Resource.Error -> {
                result.message?.let { errorMessage ->
                    result.errorCode?.let { errorCode ->
                        guessWords[index] = guessWords[index].copy(
                            errorState = GuessWordError.values()[errorCode]
                        )
                    }
                    _state.update {
                        it.copy(
                            message = errorMessage
                        )
                    }
                }

            }

            is Resource.Success -> {
                result.data?.let {
                    guessWords[index] = it
                }
            }
        }
    }

    private fun eraseLetter(index: Int) {
        when (val result = guessWords[index].eraseLetter()) {
            is Resource.Error -> {
                result.message?.let { errorMessage ->
                    result.errorCode?.let { errorCode ->
                        guessWords[index] = guessWords[index].copy(
                            errorState = GuessWordError.values()[errorCode]
                        )
                    }
                    _state.update {
                        it.copy(
                            message = errorMessage
                        )
                    }
                }

            }

            is Resource.Success -> {
                result.data?.let { guessWord ->
                    guessWords[index] = guessWord
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