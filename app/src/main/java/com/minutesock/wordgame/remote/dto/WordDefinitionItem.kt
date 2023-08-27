package com.minutesock.wordgame.remote.dto

data class WordDefinitionItem(
    val meanings: List<Meaning>,
    val origin: String,
    val phonetic: String,
    val phonetics: List<Phonetic>,
    val word: String
)