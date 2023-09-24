package com.minutesock.core.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.minutesock.core.data.models.WordSessionEntity

@Dao
interface WordSessionDao {

    @Query("SELECT * FROM WordSessionEntity")
    fun getAllSessions(): List<WordSessionEntity>

    @Query("SELECT * FROM WordSessionEntity ORDER BY id DESC LIMIT :pageSize OFFSET :offset")
    fun getPaginatedSessionsByRecency(pageSize: Int, offset: Int): List<WordSessionEntity>

    @Query("SELECT * FROM WordSessionEntity WHERE date = :date AND isDaily = 1 LIMIT 1")
    fun getTodayDailyWordSession(date: String): WordSessionEntity?

    @Query("SELECT * FROM WordSessionEntity WHERE isDaily = 0 AND gameState IN(:wordGameStates) ORDER BY id DESC LIMIT 1")
    fun getLatestInfinityWordSession(wordGameStates: List<Int>): WordSessionEntity?

    @Query("SELECT * FROM WordSessionEntity WHERE id = :id LIMIT 1")
    fun getInfinityWordSession(id: Int): WordSessionEntity?

    @Upsert
    fun insert(vararg dailyWordSessionEntities: WordSessionEntity)

    @Delete
    fun delete(wordSessionEntity: WordSessionEntity)
}