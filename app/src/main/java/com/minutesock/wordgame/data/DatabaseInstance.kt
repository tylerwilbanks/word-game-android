package com.minutesock.wordgame.data

import androidx.room.Room
import com.minutesock.wordgame.App

object DatabaseInstance {
    val db = Room.databaseBuilder(
        App.get().applicationContext,
        AppDatabase::class.java, "database-name"
    ).build()
}