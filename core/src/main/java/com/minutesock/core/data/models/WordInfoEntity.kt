package com.minutesock.core.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.minutesock.core.remote.dto.Meaning
import java.util.Date

@Entity
data class WordInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fetchDate: String? = null,
    val word: String,
    val phonetic: String? = null,
    val origin: String? = null,
    val meanings: List<Meaning>,
)