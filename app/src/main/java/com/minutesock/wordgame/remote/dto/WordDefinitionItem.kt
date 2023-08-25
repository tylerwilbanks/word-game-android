package com.minutesock.wordgame.remote.dto

import com.minutesock.wordgame.data.models.WordInfoEntity

data class WordDefinitionItem(
    val meanings: List<Meaning>,
    val origin: String,
    val phonetic: String,
    val phonetics: List<Phonetic>,
    val word: String
) {
    fun toWordInfoEntity(): WordInfoEntity {
        return WordInfoEntity(
            meanings = meanings,
            origin = origin,
            phonetic = phonetic,
            word = word
        )
    }
}