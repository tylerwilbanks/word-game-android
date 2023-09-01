package com.minutesock.core.mappers

import com.minutesock.core.data.models.DailyWordSessionEntity
import com.minutesock.core.data.models.WordInfoEntity
import com.minutesock.core.domain.DailyWordGameState
import com.minutesock.core.domain.DailyWordSession
import com.minutesock.core.domain.WordInfo
import com.minutesock.core.remote.dto.WordDefinitionItem
import com.minutesock.core.utils.toDate
import kotlinx.collections.immutable.toImmutableList
import java.text.SimpleDateFormat
import java.util.Locale


const val DATE_FORMAT_PATTERN = "dd/MM/yyyy"

fun DailyWordSessionEntity.toDailyWordSession(): DailyWordSession {
    return DailyWordSession(
        id = this.id,
        date = this.date.toDate(DATE_FORMAT_PATTERN),
        correctWord = this.correctWord,
        maxAttempts = this.maxAttempts,
        guesses = this.guesses.toImmutableList(),
        isDaily = isDaily,
        gameState = DailyWordGameState.fromInt(this.gameState)
    )
}

fun DailyWordSession.toDailyWordSessionEntity(): DailyWordSessionEntity {
    return DailyWordSessionEntity(
        id = this.id,
        date = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault()).format(this.date),
        correctWord = this.correctWord,
        maxAttempts = this.maxAttempts,
        guesses = this.guesses,
        isDaily = isDaily,
        gameState = this.gameState.ordinal
    )
}

fun WordInfoEntity.toWordInfo(): WordInfo {
    return WordInfo(
        meanings = meanings,
        word = word,
        origin = origin,
        phonetic = phonetic
    )
}

fun WordDefinitionItem.toWordInfoEntity(): WordInfoEntity {
    return WordInfoEntity(
        meanings = meanings,
        word = word,
        origin = origin,
        phonetic = phonetic
    )
}