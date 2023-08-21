package com.minutesock.wordgame.data.models

import androidx.room.Query

interface DailyWordSessionDao {

    @Query("SELECT * FROM DailyWordSession WHERE date LIKE :date LIMIT 1")
    fun getTodaySession(date: String)
}