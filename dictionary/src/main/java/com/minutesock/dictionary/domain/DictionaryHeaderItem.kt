package com.minutesock.dictionary.domain

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DictionaryHeaderItem(
    val char: Char,
    val listItems: ImmutableList<WordInfoListItem> = persistentListOf()
)
