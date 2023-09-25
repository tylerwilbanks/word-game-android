package com.minutesock.dictionary.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.core.App
import com.minutesock.core.data.repository.WordGameRepository
import com.minutesock.core.utils.Option
import com.minutesock.dictionary.domain.DictionaryDetailState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DictionaryDetailViewModel(
    private val wordGameRepository: WordGameRepository = WordGameRepository(
        wordInfoDao = App.database.WordInfoDao(),
        wordSessionDao = App.database.WordSessionDao()
    )
) : ViewModel() {

    private val _state = MutableStateFlow(DictionaryDetailState())
    val state = _state.asStateFlow()

    fun loadDictionaryDetail(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            wordGameRepository.getOrFetchWordDefinition(word).onEach { result ->
                when (result) {
                    is Option.Error -> {
                        _state.update {
                            it.copy(
                                loading = false,
                                wordInfos = persistentListOf(),
                                message = "Failed to get definition."
                            )
                        }
                    }

                    is Option.Loading -> {
                        _state.update {
                            it.copy(
                                loading = true,
                                wordInfos = result.data?.toImmutableList() ?: persistentListOf(),
                                message = "Loading..."
                            )
                        }
                    }

                    is Option.Success -> {
                        _state.update {
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
}