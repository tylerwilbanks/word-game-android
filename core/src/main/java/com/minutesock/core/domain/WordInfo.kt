package com.minutesock.core.domain

import com.minutesock.core.remote.dto.Meaning

data class WordInfo(
    val word: String,
    val phonetic: String? = null,
    val origin: String? = null,
    val meanings: List<com.minutesock.core.remote.dto.Meaning>,
)