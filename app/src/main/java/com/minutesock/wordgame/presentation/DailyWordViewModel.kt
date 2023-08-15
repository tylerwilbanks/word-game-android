package com.minutesock.wordgame.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.wordgame.R
import com.minutesock.wordgame.domain.DailyWordValidationResultType
import com.minutesock.wordgame.domain.GuessKey
import com.minutesock.wordgame.domain.GuessLetter
import com.minutesock.wordgame.domain.GuessWord
import com.minutesock.wordgame.domain.GuessWordState
import com.minutesock.wordgame.domain.GuessWordValidator
import com.minutesock.wordgame.domain.LetterState
import com.minutesock.wordgame.domain.addGuessLetter
import com.minutesock.wordgame.domain.eraseLetter
import com.minutesock.wordgame.domain.lockInGuess
import com.minutesock.wordgame.domain.updateState
import com.minutesock.wordgame.uiutils.UiText
import com.minutesock.wordgame.utils.Option
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DailyWordViewModel : ViewModel() {

    private val _state = MutableStateFlow(DailyWordState())
    val state = _state.asStateFlow()

    val guessWords = mutableStateListOf<GuessWord>()

    private var dailyWordStateMessage = DailyWordStateMessage()

    private val falseKeyboardKeys: ImmutableList<GuessKey>
        get() {
            val keys = hashMapOf<Char, LetterState>()
            guessWords.forEach { guessWord ->
                guessWord.letters.forEach { guessLetter ->
                    val maxOrdinal = keys.filter {
                        it.key == guessLetter.character && it.value.ordinal > guessLetter.state.ordinal
                    }.values.maxOfOrNull { it.ordinal }
                    val newState = if (maxOrdinal != null) {
                        LetterState.values()[maxOrdinal]
                    } else {
                        guessLetter.state
                    }
                    keys[guessLetter.character] = newState
                }
            }
            return keys.map { GuessKey(it.key, it.value) }.toImmutableList()
        }

    fun setupGame(wordLength: Int = 5, maxGuessAttempts: Int = 1) {
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
                    correctWord = GuessWordValidator.obtainRandomWord(),
                    dailyWordStateMessage = DailyWordStateMessage(
                        uiText = UiText.StringResource(R.string.what_in_da_word)
                    )
                )
            }
        }
    }

    fun onEvent(event: DailyWordEvent) {
        if (state.value.gameState == DailyWordGameState.NotStarted
        ) {
            return
        }
        when (event) {
            is DailyWordEvent.OnCharacterPress -> {
                if (state.value.gameState.isGameOver) {
                    return
                }
                viewModelScope.launch {
                    getCurrentGuessWordIndexAndHandleError()?.let { index ->
                        updateCurrentGuessWord(index, event.character)
                    }
                }
            }

            DailyWordEvent.OnDeletePress -> {
                if (state.value.gameState.isGameOver) {
                    return
                }
                viewModelScope.launch {
                    getCurrentGuessWordIndexAndHandleError()?.let { index ->
                        eraseLetter(index)
                    }
                }
            }

            DailyWordEvent.OnEnterPress -> {
                if (state.value.gameState.isGameOver) {
                    return
                }
                viewModelScope.launch {
                    getCurrentGuessWordIndexAndHandleError()?.let { index ->
                        val currentGuessWord = guessWords[index]
                        val result = GuessWordValidator.validateGuess(
                            guessWord = currentGuessWord,
                            correctWord = state.value.correctWord!!,
                            isFinalGuess = isFinalGuess(index)
                        )
                        when (result.type) {
                            DailyWordValidationResultType.Unknown -> {}
                            DailyWordValidationResultType.Error -> {
                                displayError(result.uiText)
                            }

                            DailyWordValidationResultType.Incorrect -> {
                                guessWords[index] =
                                    guessWords[index].lockInGuess(state.value.correctWord!!)
                                dailyWordStateMessage = DailyWordStateMessage(
                                    uiText = result.uiText,
                                    isError = isFinalGuess(index)
                                )
                                _state.update {
                                    it.copy(
                                        falseKeyboardKeys = falseKeyboardKeys
                                    )
                                }

                                if (isFinalGuess(index)) {
                                    _state.update {
                                        it.copy(
                                            gameState = DailyWordGameState.Failure,
                                            falseKeyboardKeys = falseKeyboardKeys
                                        )
                                    }
                                } else {
                                    guessWords[index + 1] = guessWords[index + 1].copy(
                                        state = GuessWordState.Editing
                                    )
                                }

                            }

                            DailyWordValidationResultType.Success -> {
                                guessWords[index] =
                                    guessWords[index].lockInGuess(state.value.correctWord!!)
                                dailyWordStateMessage = DailyWordStateMessage(
                                    uiText = result.uiText,
                                    isError = false
                                )
                                _state.update {
                                    it.copy(
                                        falseKeyboardKeys = falseKeyboardKeys,
                                        gameState = DailyWordGameState.Success,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            DailyWordEvent.OnErrorAnimationFinished -> {
                if (state.value.gameState.isGameOver) {
                    return;
                }
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

            DailyWordEvent.OnAnsweredWordRowAnimationFinished -> {
                viewModelScope.launch {
                    if (state.value.gameState.isGameOver) {
                        guessWords.indexOfLast { it.state == GuessWordState.Complete }
                            .let { index ->
                                guessWords[index] =
                                    guessWords[index].updateState(
                                        if (state.value.gameState == DailyWordGameState.Success){
                                            GuessWordState.Correct

                                        }
                                    else {
                                        GuessWordState.Failure
                                        }
                                    )
                            }
                    }
                    _state.update {
                        it.copy(
                            dailyWordStateMessage = dailyWordStateMessage
                        )
                    }
                }
            }

        }
    }

    private fun isFinalGuess(index: Int): Boolean = index + 1 == guessWords.size

    private fun displayError(uiText: UiText, guessWordError: GuessWordError? = null) {
        _state.update {
            it.copy(
                dailyWordStateMessage = DailyWordStateMessage(
                    uiText = uiText,
                    isError = true
                )
            )
        }

        getCurrentGuessWordIndexAndHandleError()?.let { index ->
            guessWords[index] = guessWords[index].copy(
                errorState = guessWordError ?: GuessWordError.Unknown
            )
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

    private fun getCurrentGuessWordIndexAndHandleError(): Int? {
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

            else -> {
                /* ignore Option.Error */
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