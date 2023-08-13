package com.minutesock.wordgame.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.wordgame.R
import com.minutesock.wordgame.domain.GuessLetter
import com.minutesock.wordgame.domain.GuessWord
import com.minutesock.wordgame.domain.GuessWordState
import com.minutesock.wordgame.domain.addGuessLetter
import com.minutesock.wordgame.domain.eraseLetter
import com.minutesock.wordgame.domain.updateState
import com.minutesock.wordgame.uiutils.UiText
import com.minutesock.wordgame.utils.Option
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DailyWordViewModel : ViewModel() {

    private val _state = MutableStateFlow(DailyWordState())
    val state = _state.asStateFlow()

    val guessWords = mutableStateListOf<GuessWord>()

    fun setupGame(wordLength: Int = 5, maxGuessAttempts: Int = 5) {
        if (state.value.gameState == DailyWordGameState.NotStarted) {
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
                    gameState = DailyWordGameState.InProgress,
                    wordLength = wordLength,
                    maxGuessAttempts = maxGuessAttempts,
                    correctWord = "smack",
                    dailyWordStateMessage = DailyWordStateMessage(
                        uiText = UiText.StringResource(R.string.what_in_da_word)
                    )
                )
            }
        }
    }

    fun onEvent(event: DailyWordEvent) {
        if (state.value.gameState == DailyWordGameState.NotStarted ||
                state.value.gameState == DailyWordGameState.Complete) {
            return
        }
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
                            dailyWordStateMessage = DailyWordStateMessage(
                                uiText = UiText.StringResource(R.string.what_in_da_word)
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getCurrentGuessWordIndex(): Option<Int> {
        val index = guessWords.indexOfFirst {
            it.state == GuessWordState.Editing
        }
        return if (index == -1) {
            Option.UiError(
                uiText = UiText.StringResource(R.string.there_is_no_more_guess_attempts_left)
            )
        } else {
            Option.Success(index)
        }
    }

    private fun getCurrentGuessWordAndHandleError(): Int? {
        return when (val result = getCurrentGuessWordIndex()) {
            is Option.UiError -> {
                result.uiText?.let { uiText ->
                    _state.update {
                        it.copy(
                            dailyWordStateMessage = DailyWordStateMessage(
                                uiText = uiText,
                                isError = true
                            )
                        )
                    }
                }
                null
            }

            is Option.Success -> result.data

            else -> {
                /* ignore Option.Error */
                null
            }
        }
    }

    private fun updateCurrentGuessWord(index: Int, character: Char) {
        val result = guessWords[index].addGuessLetter(
            GuessLetter(_character = character)
        )
        when (result) {
            is Option.UiError -> {
                result.uiText?.let { uiTextError ->
                    result.errorCode?.let { errorCode ->
                        guessWords[index] = guessWords[index].copy(
                            errorState = GuessWordError.values()[errorCode]
                        )
                    }
                    _state.update {
                        it.copy(
                            dailyWordStateMessage = DailyWordStateMessage(
                                uiText = uiTextError,
                                isError = true
                            )
                        )
                    }
                }

            }

            is Option.Success -> {
                result.data?.let {
                    guessWords[index] = it
                }
            }

            else -> {/* ignore Option.Error */
            }
        }
    }

    private fun eraseLetter(index: Int) {
        when (val result = guessWords[index].eraseLetter()) {
            is Option.UiError -> {
                result.uiText?.let { uiText ->
                    result.errorCode?.let { errorCode ->
                        guessWords[index] = guessWords[index].copy(
                            errorState = GuessWordError.values()[errorCode]
                        )
                    }
                    _state.update {
                        it.copy(
                            dailyWordStateMessage = DailyWordStateMessage(
                                uiText = uiText,
                                isError = true
                            )
                        )
                    }
                }

            }

            is Option.Success -> {
                result.data?.let { guessWord ->
                    guessWords[index] = guessWord
                }
            }

            else -> {/* ignore Option.Error */
            }
        }
    }
}