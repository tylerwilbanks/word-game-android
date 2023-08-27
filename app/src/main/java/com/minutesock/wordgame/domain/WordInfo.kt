package com.minutesock.wordgame.domain

import com.minutesock.wordgame.remote.dto.Meaning

data class WordInfo(
    val word: String,
    val phonetic: String,
    val origin: String? = null,
    val meanings: List<Meaning>,
)