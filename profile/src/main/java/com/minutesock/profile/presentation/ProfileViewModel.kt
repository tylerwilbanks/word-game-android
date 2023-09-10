package com.minutesock.profile.presentation

import ProfileRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.core.App
import com.minutesock.profile.domain.GuessDistributionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository = ProfileRepository(dailyWordSessionDao = App.database.DailyWordSessionDao())
) : ViewModel() {

    private val _guessDistributionState = MutableStateFlow(GuessDistributionState())
    val guessDistributionState = _guessDistributionState.asStateFlow()

    init {
        updateGuessDistribution()
    }

    fun updateGuessDistribution() {
        viewModelScope.launch {
            profileRepository.getGuessDistribution().onEach { incomingGuessDistributionState ->
                _guessDistributionState.update {
                    incomingGuessDistributionState
                }
            }
        }
    }
}