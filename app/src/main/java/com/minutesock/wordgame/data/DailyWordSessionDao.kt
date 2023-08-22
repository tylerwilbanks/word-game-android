package com.minutesock.wordgame.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.minutesock.wordgame.data.models.DailyWordSession

@Dao
interface DailyWordSessionDao {

    @Query("SELECT * FROM DailyWordSession WHERE date LIKE :date LIMIT 1")
    fun getTodaySession(date: String) : DailyWordSession?

    @Insert
    fun insert(vararg dailyWordSessions: DailyWordSession)

    @Delete
    fun delete(dailyWordSession: DailyWordSession)
}