package com.minutesock.dictionary.data.repository

import com.minutesock.core.data.WordInfoDao
import com.minutesock.core.data.WordSessionDao
import com.minutesock.core.domain.GuessWordValidator
import com.minutesock.dictionary.domain.DictionaryHeaderItem
import com.minutesock.dictionary.domain.DictionaryState
import com.minutesock.dictionary.domain.WordInfoListItem
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flow

class DictionaryRepository(
    private val wordSessionDao: WordSessionDao,
) {

    fun getAlphabeticalDictionaryState() = flow {
        val groupedCorrectWords = wordSessionDao.getCompletedCorrectWordsSortedAlphabetically().groupBy { it }
        val listItems = groupedCorrectWords.map {
            WordInfoListItem(
                word = it.key,
                sessionCount = it.value.size
            )
        }
        val headers = listItems.groupBy { it.word.first() }
        val headerItems = headers.map {
            DictionaryHeaderItem(
                char = it.key.uppercaseChar(),
                listItems = it.value.toImmutableList()
            )
        }
        emit(
            DictionaryState(
                unlockedWordCount = headerItems.sumOf { it.listItems.size },
                totalWordCount = GuessWordValidator.wordSelectionCount,
                headerItems = headerItems.toImmutableList()
            )
        )
    }
}