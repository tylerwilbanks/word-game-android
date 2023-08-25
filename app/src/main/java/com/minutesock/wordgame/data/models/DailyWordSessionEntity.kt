package com.minutesock.wordgame.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class DailyWordSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val date: String,
    val correctWord: String,
    val maxAttempts: Int,
)
