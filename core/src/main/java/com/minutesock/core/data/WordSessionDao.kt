package com.minutesock.core.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.minutesock.core.data.models.WordSessionEntity
import com.minutesock.core.domain.WordGameState

@Dao
interface WordSessionDao {

    @Query("SELECT * FROM WordSessionEntity")
    fun getAllSessions(): List<WordSessionEntity>

    @Query("SELECT correctWord FROM WordSessionEntity WHERE gameState in(:wordGameStates) ORDER BY correctWord ASC")
    fun getCompletedCorrectWordsSortedAlphabetically(
        wordGameStates: List<Int> = listOf(
            WordGameState.Success.ordinal,
            WordGameState.Failure.ordinal
        )
    ): List<String>

    @Query("SELECT * FROM WordSessionEntity WHERE id < :lastFetchedItemId ORDER BY id DESC LIMIT :pageSize")
    fun getPaginatedSessionsByRecency(
        pageSize: Int,
        lastFetchedItemId: Int
    ): List<WordSessionEntity>

    @Query("SELECT * FROM WordSessionEntity WHERE date = :date AND isDaily = 1 LIMIT 1")
    fun getTodayDailyWordSession(date: String): WordSessionEntity?

    @Query("SELECT * FROM WordSessionEntity WHERE isDaily = 0 AND gameState IN(:wordGameStates) ORDER BY id DESC LIMIT 1")
    fun getLatestInfinityWordSession(wordGameStates: List<Int>): WordSessionEntity?

    @Query("SELECT * FROM WordSessionEntity WHERE id = :id LIMIT 1")
    fun getInfinityWordSession(id: Int): WordSessionEntity?

    @Query("SELECT * FROM WordSessionEntity WHERE correctWord = :word ORDER BY id DESC")
    fun getAllWordSessionsWithWord(word: String): List<WordSessionEntity>

    @Query("SELECT COUNT(*) FROM WordSessionEntity WHERE gameState IN(:wordGameStates)")
    fun getCompletedWordSessionCount(wordGameStates: List<Int>): Int

    @Upsert
    fun insert(vararg dailyWordSessionEntities: WordSessionEntity)

    @Delete
    fun delete(wordSessionEntity: WordSessionEntity)
}