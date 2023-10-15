package com.minutesock.daily.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.core.App
import com.minutesock.core.data.repository.WordGameRepository
import com.minutesock.core.domain.WordEventGame
import com.minutesock.core.domain.WordEventStats
import com.minutesock.core.domain.WordGameNotStartedEvent
import com.minutesock.core.presentation.WordGameLogicHelper
import kotlinx.coroutines.launch

class DailyWordViewModel(
    wordGameRepository: WordGameRepository = WordGameRepository(
        wordInfoDao = App.database.WordInfoDao(),
        wordSessionDao = App.database.WordSessionDao()
    )
) : ViewModel() {

    private val wordGameLogicHelper = WordGameLogicHelper(
        wordGameRepository = wordGameRepository
    )

    val state = wordGameLogicHelper.state

    val completedWordSessionCount = wordGameLogicHelper.completedWordSessionCount

    fun onWordGameNotStartedEvent(event: WordGameNotStartedEvent) {
        viewModelScope.launch {
            wordGameLogicHelper.onWordGameNotStartedEvent(event)
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