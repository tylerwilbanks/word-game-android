package com.minutesock.core.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.minutesock.core.data.models.DailyWordSessionEntity

@Dao
interface DailyWordSessionDao {

    @Query("SELECT * FROM DailyWordSessionEntity WHERE date = :date LIMIT 1")
    fun getTodaySession(date: String): DailyWordSessionEntity?

    @Upsert
    fun insert(vararg dailyWordSessionEntities: DailyWordSessionEntity)

    @Delete
    fun delete(dailyWordSessionEntity: DailyWordSessionEntity)
}