package com.minutesock.wordgame.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class DailyWordSession(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val wordLength: Int,
)
