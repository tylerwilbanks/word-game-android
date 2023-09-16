package com.minutesock.core.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.minutesock.core.data.models.DailyWordSessionEntity
import java.util.Date

@Dao
interface DailyWordSessionDao {

    @Query("SELECT * FROM DailyWordSessionEntity")
    fun getAllSessions(): List<DailyWordSessionEntity>

    @Query("SELECT * FROM DailyWordSessionEntity ORDER BY date DESC LIMIT :pageSize OFFSET :offset")
    fun getPaginatedSessionsByRecency(pageSize: Int, offset: Int): List<DailyWordSessionEntity>

    @Query("SELECT * FROM DailyWordSessionEntity WHERE date = :date LIMIT 1")
    fun getTodaySession(date: String): DailyWordSessionEntity?

    @Upsert
    fun insert(vararg dailyWordSessionEntities: DailyWordSessionEntity)

    @Delete
    fun delete(dailyWordSessionEntity: DailyWordSessionEntity)
}