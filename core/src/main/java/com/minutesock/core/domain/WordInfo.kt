package com.minutesock.core.domain

data class WordInfo(
    val word: String,
    val phonetic: String? = null,
    val origin: String? = null,
    val meanings: List<com.minutesock.core.remote.dto.Meaning>,
)