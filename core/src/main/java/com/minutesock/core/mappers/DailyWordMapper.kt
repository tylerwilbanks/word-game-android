package com.minutesock.core.mappers

import com.minutesock.core.data.GuessWordStorage
import com.minutesock.core.data.models.WordInfoEntity
import com.minutesock.core.data.models.WordSessionEntity
import com.minutesock.core.domain.GuessWord
import com.minutesock.core.domain.GuessWordRowInfoView
import com.minutesock.core.domain.WordGameMode
import com.minutesock.core.domain.WordGameState
import com.minutesock.core.domain.WordInfo
import com.minutesock.core.domain.WordSession
import com.minutesock.core.domain.WordSessionInfoView
import com.minutesock.core.remote.dto.WordDefinitionItem
import com.minutesock.core.utils.toDate
import com.minutesock.core.utils.toString
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


const val DATE_FORMAT_PATTERN = "dd/MM/yyyy"

fun WordSessionEntity.toWordSession(): WordSession {
    return WordSession(
        id = this.id,
        date = this.date.toDate(DATE_FORMAT_PATTERN),
        correctWord = this.correctWord,
        maxAttempts = this.maxAttempts,
        guesses = this.guesses.map { it.toGuessWord() }.toImmutableList(),
        isDaily = isDaily,
        gameState = WordGameState.fromInt(this.gameState),
        startTime = if (startTime != null) Instant.parse(startTime) else null
    )
}

fun WordSession.toWordSessionEntity(): WordSessionEntity {
    return WordSessionEntity(
        id = this.id,
        date = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault()).format(this.date),
        correctWord = this.correctWord,
        maxAttempts = this.maxAttempts,
        guesses = this.guesses.map { it.toGuessWordStorage() }.toList(),
        isDaily = isDaily,
        gameState = this.gameState.ordinal,
        startTime = startTime?.toString()

    )
}

fun WordSessionEntity.toWordSessionInfoView(): WordSessionInfoView {
    val w = this.toWordSession()
    return WordSessionInfoView(
        displayDate = w.formattedTime?.toString() ?: "",
        guessWordRowInfoViews = w.guesses.mapIndexed { index: Int, guessWord: GuessWord ->
            GuessWordRowInfoView(
                guessWord = guessWord,
                displayTimestamp = w.getFormattedIndividualElapsedTime(index)
            )
        }.toImmutableList(),
        displayCompleteTime = w.formattedElapsedTime,
        gameMode = if (w.isDaily) WordGameMode.Daily else WordGameMode.Inifinity,
        gameState = w.gameState
    )
}

fun WordInfoEntity.toWordInfo(): WordInfo {
    return WordInfo(
        fetchDate = fetchDate?.toDate(DATE_FORMAT_PATTERN),
        meanings = meanings,
        word = word,
        origin = origin,
        phonetic = phonetic
    )
}

fun WordDefinitionItem.toWordInfoEntity(fetchDate: Date? = null): WordInfoEntity {
    return WordInfoEntity(
        fetchDate = fetchDate?.toString(DATE_FORMAT_PATTERN),
        meanings = meanings,
        word = word,
        origin = origin,
        phonetic = phonetic
    )
}

fun GuessWordStorage.toGuessWord(): GuessWord {
    return GuessWord(
        letters = this.letters.toImmutableList(),
        state = this.state,
        errorState = this.errorState,
        completeTime = if (this.completeTime != null) Instant.parse(this.completeTime) else null
    )
}

fun GuessWord.toGuessWordStorage(): GuessWordStorage {
    return GuessWordStorage(
        letters = this.letters.toList(),
        state = this.state,
        errorState = this.errorState,
        completeTime = this.completeTime?.toString()
    )
}