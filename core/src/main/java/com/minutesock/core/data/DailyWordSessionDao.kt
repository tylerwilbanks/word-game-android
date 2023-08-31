package com.minutesock.core.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.minutesock.core.data.models.DailyWordSessionEntity

@Dao
interface DailyWordSessionDao {

    @Query("SELECT * FROM DailyWordSessionEntity WHERE date LIKE :date LIMIT 1")
    fun getTodaySession(date: String): DailyWordSessionEntity?

    @Insert
    fun insert(vararg dailyWordSessionEntities: DailyWordSessionEntity)

    @Delete
    fun delete(dailyWordSessionEntity: DailyWordSessionEntity)
}