package com.minutesock.wordgame.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.minutesock.wordgame.data.models.DailyWordSession

@Database(entities = [DailyWordSession::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun DailyWordSessionDao() : DailyWordSessionDao
}