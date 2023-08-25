package com.minutesock.wordgame.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.minutesock.wordgame.domain.WordInfo
import com.minutesock.wordgame.remote.dto.Meaning

@Entity
data class WordInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val word: String,
    val phonetic: String,
    val origin: String,
    val meanings: List<Meaning>,
) {
    fun toWordInfo(): WordInfo {
        return WordInfo(
            meanings = meanings,
            word = word,
            origin = origin,
            phonetic = phonetic
        )
    }
}