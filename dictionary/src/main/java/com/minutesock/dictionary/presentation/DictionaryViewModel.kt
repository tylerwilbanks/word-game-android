package com.minutesock.dictionary.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.core.App
import com.minutesock.dictionary.data.repository.DictionaryRepository
import com.minutesock.dictionary.domain.DictionaryEvent
import com.minutesock.dictionary.domain.DictionaryState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DictionaryViewModel(
    private val dictionaryRepository: DictionaryRepository = DictionaryRepository(
        wordSessionDao = App.database.WordSessionDao()
    )
) : ViewModel() {


    private val _state = MutableStateFlow(DictionaryState())
    val state = _state.asStateFlow()

    init {
        updateList()
    }

    fun updateList() {
        viewModelScope.launch(Dispatchers.IO) {
            dictionaryRepository.getAlphabeticalDictionaryState().onEach { newState ->
                _state.update {
                    newState
                }
            }.launchIn(this)
        }
    }

    fun loadDictionaryDetail(word: String) {

    }

    fun onEvent(event: DictionaryEvent) {
        when (event) {
            is DictionaryEvent.OnWordInfoListItemClicked -> {
                //todo navigate to dictionary detail
            }
        }
    }
}