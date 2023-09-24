package com.minutesock.dictionary.domain

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DictionaryState(
    val unlockedWordCount: Int = 0,
    val totalWordCount: Int = 0,
    val headerItems: ImmutableList<DictionaryHeaderItem> = persistentListOf()
)
