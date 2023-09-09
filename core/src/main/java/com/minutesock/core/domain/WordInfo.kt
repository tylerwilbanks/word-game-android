package com.minutesock.core.domain

import com.minutesock.core.remote.dto.Meaning
import java.util.Date

data class WordInfo(
    val fetchDate: Date? = null,
    val word: String,
    val phonetic: String? = null,
    val origin: String? = null,
    val meanings: List<Meaning>,
)