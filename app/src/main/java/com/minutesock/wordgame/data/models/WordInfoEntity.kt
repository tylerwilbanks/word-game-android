package com.minutesock.wordgame.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.minutesock.wordgame.remote.dto.Meaning

@Entity
data class WordInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val word: String,
    val phonetic: String,
    val origin: String? = null,
    val meanings: List<Meaning>,
)