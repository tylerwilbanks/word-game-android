package com.minutesock.dictionary.domain

import com.minutesock.core.domain.WordInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DictionaryDetailState(
    val loading: Boolean = false,
    val wordInfos: ImmutableList<WordInfo> = persistentListOf(),
    val message: String? = null,
)
