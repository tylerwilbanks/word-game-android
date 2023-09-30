package com.minutesock.wordgame.presentation

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.core.App
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode = _isDarkMode.asStateFlow()

    fun toggleDarkMode(toggle: Boolean) {
        viewModelScope.launch {
            val mode = when (toggle) {
                true -> Configuration.UI_MODE_NIGHT_YES
                false -> Configuration.UI_MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(mode)
            _isDarkMode.update { toggle }
        }
    }
}