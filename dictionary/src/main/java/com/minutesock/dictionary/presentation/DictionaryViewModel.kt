package com.minutesock.dictionary.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.core.App
import com.minutesock.core.data.repository.WordGameRepository
import com.minutesock.core.domain.WordInfo
import com.minutesock.core.utils.Option
import com.minutesock.dictionary.data.repository.DictionaryRepository
import com.minutesock.dictionary.domain.DictionaryDetailState
import com.minutesock.dictionary.domain.DictionaryEvent
import com.minutesock.dictionary.domain.DictionaryState
import com.minutesock.dictionary.navigation.navigateToDictionaryDetail
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
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
    ),
    private val wordGameRepository: WordGameRepository = WordGameRepository(
        wordInfoDao = App.database.WordInfoDao(),
        wordSessionDao = App.database.WordSessionDao()
    )
) : ViewModel() {


    private val _stateList = MutableStateFlow(DictionaryState())
    val stateList = _stateList.asStateFlow()

    private val _stateWordInfo = MutableStateFlow(DictionaryDetailState())
    val stateWordInfo = _stateWordInfo.asStateFlow()

    init {
        updateList()
    }

    fun updateList() {
        viewModelScope.launch(Dispatchers.IO) {
            dictionaryRepository.getAlphabeticalDictionaryState().onEach { newState ->
                _stateList.update {
                    newState
                }
            }.launchIn(this)
        }
    }

    fun loadDictionaryDetail(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            wordGameRepository.getOrFetchWordDefinition(word).onEach { result ->
                when (result) {
                    is Option.Error -> {
                        _stateWordInfo.update {
                            it.copy(
                                loading = false,
                                wordInfos = persistentListOf(),
                                message = "Failed to get definition."
                            )
                        }
                    }
                    is Option.Loading -> {
                        _stateWordInfo.update {
                            it.copy(
                                loading = true,
                                wordInfos = result.data?.toImmutableList() ?: persistentListOf(),
                                message = "Loading..."
                            )
                        }
                    }
                    is Option.Success -> {
                        _stateWordInfo.update {
                            it.copy(
                                loading = false,
                                wordInfos = result.data?.toImmutableList() ?: persistentListOf()
                            )
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    fun onEvent(event: DictionaryEvent) {
        when (event) {
            is DictionaryEvent.OnWordInfoListItemClicked -> {
                loadDictionaryDetail(event.word)
                event.navController.navigateToDictionaryDetail(event.word)
            }
        }
    }
}