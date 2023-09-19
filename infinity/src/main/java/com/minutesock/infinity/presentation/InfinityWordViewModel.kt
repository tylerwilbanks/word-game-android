package com.minutesock.infinity.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.core.App
import com.minutesock.core.data.repository.WordGameRepository
import com.minutesock.core.domain.WordGameMode
import com.minutesock.core.presentation.WordEventGame
import com.minutesock.core.presentation.WordEventStats
import com.minutesock.core.presentation.WordGameLogicHelper
import kotlinx.coroutines.launch

class InfinityWordViewModel(
    wordGameRepository: WordGameRepository = WordGameRepository(
        wordInfoDao = App.database.WordInfoDao(),
        wordSessionDao = App.database.WordSessionDao()
    )
) : ViewModel() {

    private val wordGameLogicHelper = WordGameLogicHelper(
        wordGameRepository = wordGameRepository
    )

    val state = wordGameLogicHelper.state

    fun setupGame(wordLength: Int = 5, maxGuessAttempts: Int = 6) {
        viewModelScope.launch {
            wordGameLogicHelper.setupGame(WordGameMode.Inifinity, wordLength, maxGuessAttempts)
        }
    }

    fun onGameEvent(event: WordEventGame) {
        viewModelScope.launch {
            wordGameLogicHelper.onGameEvent(event)
        }
    }

    fun onStatEvent(event: WordEventStats) {
        viewModelScope.launch {
            wordGameLogicHelper.onStatsEvent(event)
        }
    }
}