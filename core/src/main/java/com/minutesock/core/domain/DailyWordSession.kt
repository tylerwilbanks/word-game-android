package com.minutesock.core.domain

import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Date
import kotlin.time.Duration

data class DailyWordSession(
    val id: Int = 0,
    val date: Date,
    val correctWord: String,
    val maxAttempts: Int,
    val guesses: ImmutableList<GuessWord>,
    val isDaily: Boolean,
    val gameState: DailyWordGameState,
    val startTime: Instant? = null
) {
    val wordLength = correctWord.length

    val completeTime = guesses.findLast { it.completeTime != null }?.completeTime

    val formattedTime = completeTime?.toLocalDateTime(TimeZone.currentSystemDefault())?.date

    val successTime =
        guesses.lastOrNull { it.completeTime != null && it.state == GuessWordState.Correct }?.completeTime

    val emojiRepresentation: String
        get() {
            val finalIndex =
                guesses.indexOfFirst { it.state == GuessWordState.Correct || it.state == GuessWordState.Failure }
            var text = ""
            guesses.forEachIndexed { index, guessWord ->
                if (index > finalIndex) {
                    return@forEachIndexed
                }
                guessWord.letters.forEach {
                    text += it.state.emoji
                }
                text += "\n"
            }
            return text.trimEnd('\n')
        }

    val shareText: String
        get() {
            val finalIndex =
                guesses.indexOfFirst { it.state == GuessWordState.Correct || it.state == GuessWordState.Failure }
            val resultLetter =
                if (finalIndex + 1 >= guesses.size && gameState == DailyWordGameState.Failure) "X" else "${finalIndex + 1}"

            return "$resultLetter/${guesses.size}\n" +
                    "$formattedElapsedTime\n" +
                    emojiRepresentation
        }

    val elapsedTime: Duration
        get() {
            val firstGuess =
                guesses[0].state == GuessWordState.Correct || guesses[0].state == GuessWordState.Failure
            val incomplete =
                gameState == DailyWordGameState.NotStarted || gameState == DailyWordGameState.InProgress
            if (firstGuess || incomplete) {
                return Duration.ZERO
            }
            val startTime = guesses.find { it.completeTime != null }?.completeTime
            val endTime = guesses.findLast { it.completeTime != null }?.completeTime
            if (startTime != null && endTime != null) {
                return endTime.minus(startTime)
            }
            return Duration.INFINITE
        }

    val formattedElapsedTime: String
        get() = elapsedTime.toComponents { minutes, seconds, nanoseconds ->
            "${minutes}m ${seconds}s"
        }.toString()
}