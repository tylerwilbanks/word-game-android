package com.minutesock.wordgame.presentation

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.wordgame.R
import com.minutesock.wordgame.domain.GuessLetter
import com.minutesock.wordgame.domain.GuessWord
import com.minutesock.wordgame.domain.addGuessLetter
import kotlinx.collections.immutable.persistentListOf
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
        w[0] = w[0].addGuessLetter(GuessLetter(_character = 'C'))
            ?: GuessWord(letters = persistentListOf())
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
                }
            }

            DailyWordEvent.OnDeletePress -> {
                viewModelScope.launch {
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