package com.minutesock.wordgame.mappers

import com.minutesock.wordgame.data.models.DailyWordSessionEntity
import com.minutesock.wordgame.data.models.WordInfoEntity
import com.minutesock.wordgame.domain.UserDailyWordSession
import com.minutesock.wordgame.domain.WordInfo
import com.minutesock.wordgame.remote.dto.WordDefinitionItem
import com.minutesock.wordgame.utils.toDate
import java.text.SimpleDateFormat
import java.util.Locale

const val DATE_FORMAT_PATTERN = "dd/MM/yyyy"

fun DailyWordSessionEntity.toUserDailyWordSession(): UserDailyWordSession {
    return UserDailyWordSession(
        date = this.date.toDate(DATE_FORMAT_PATTERN),
        correctWord = this.correctWord,
        maxAttempts = this.maxAttempts
    )
}

fun UserDailyWordSession.toDailyWordSession(): DailyWordSessionEntity {
    return DailyWordSessionEntity(
        id = 0,
        date = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault()).format(this.date),
        correctWord = this.correctWord,
        maxAttempts = this.maxAttempts
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