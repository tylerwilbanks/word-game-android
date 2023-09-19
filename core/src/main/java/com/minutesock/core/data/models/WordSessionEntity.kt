package com.minutesock.core.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.minutesock.core.data.GuessWordStorage

@Entity
data class WordSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val correctWord: String,
    val maxAttempts: Int,
    val guesses: List<GuessWordStorage>,
    val isDaily: Boolean,
    val gameState: Int,
    val startTime: String? = null,
)
