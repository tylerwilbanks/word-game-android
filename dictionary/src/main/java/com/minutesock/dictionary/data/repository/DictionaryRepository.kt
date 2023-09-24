package com.minutesock.dictionary.data.repository

import com.minutesock.core.data.WordInfoDao
import com.minutesock.core.domain.GuessWordValidator
import com.minutesock.core.utils.capitalize
import com.minutesock.dictionary.domain.DictionaryHeaderItem
import com.minutesock.dictionary.domain.DictionaryState
import com.minutesock.dictionary.domain.WordInfoListItem
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flow

class DictionaryRepository(
    private val wordInfoDao: WordInfoDao,
) {

    fun getAlphabeticalDictionaryState() = flow {
        val groupedWordInfos = wordInfoDao.getAllWordInfosSortedAlphabetically().groupBy { it.word }
        val listItems = groupedWordInfos.map { WordInfoListItem(
            word = it.key,
            sessionCount = it.value.size
        ) }
        val headers = listItems.groupBy { it.word.first() }
        val headerItems = headers.map { DictionaryHeaderItem(
            char = it.key.uppercaseChar(),
            listItems = it.value.toImmutableList()
        ) }
        emit(DictionaryState(
            unlockedWordCount = groupedWordInfos.size,
            totalWordCount = GuessWordValidator.wordSelectionCount,
            headerItems = headerItems.toImmutableList()
        ))
    }
}