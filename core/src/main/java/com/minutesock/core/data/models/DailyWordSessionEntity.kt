package com.minutesock.core.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.minutesock.core.domain.GuessWord

@Entity
data class DailyWordSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val correctWord: String,
    val maxAttempts: Int,
    val guesses: List<GuessWord>,
    val isDaily: Boolean,
    val gameState: Int
)
