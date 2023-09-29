package com.minutesock.profile.presentation

import ProfileRepository
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.core.App
import com.minutesock.core.domain.WordSession
import com.minutesock.profile.domain.GuessDistributionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository = ProfileRepository(wordSessionDao = App.database.WordSessionDao())
) : ViewModel() {

    private val _guessDistributionState = MutableStateFlow(GuessDistributionState())
    val guessDistributionState = _guessDistributionState.asStateFlow()

    private var currentHistoryPage = mutableStateOf(0)

    val historyList = mutableStateListOf<WordSession>()
    private val historyPageSize = 20

    private var historyScrollPosition = 0
    private var lastFetchedId = 100_000_000

    init {
        updateGuessDistribution()
    }

    fun onHistoryEvent(event: HistoryScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is HistoryScreenEvent.UpdateScrollPosition -> {
                    historyScrollPosition = event.scrollPosition
                    Log.e("shovel", "HIStory scroll position: ${historyScrollPosition}")
                    if (historyScrollPosition + 1 >= (currentHistoryPage.value * historyPageSize)) {
                        loadPaginatedHistory()
                    }
                }
            }
        }
    }

    fun updateGuessDistribution() {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.getGuessDistribution().onEach { incomingGuessDistributionState ->
                _guessDistributionState.update {
                    incomingGuessDistributionState
                }
            }.launchIn(this)
        }
    }

    fun loadPaginatedHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            if (historyScrollPosition + 1 >= (currentHistoryPage.value * historyPageSize)) {
                Log.e("shovel", "loading new paginated history. page#: ${currentHistoryPage.value}")
                profileRepository.getDailyWordSessions(historyPageSize, lastFetchedId)
                    .onEach { newList: List<WordSession> ->
                        historyList.addAll(newList)
                        currentHistoryPage.value += 1
                        newList.lastOrNull()?.let {
                            lastFetchedId = it.id
                        }
                    }.launchIn(this)
            }
        }
    }
}