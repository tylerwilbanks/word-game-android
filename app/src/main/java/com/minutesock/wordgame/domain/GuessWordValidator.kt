package com.minutesock.wordgame.domain

import android.content.Context
import com.minutesock.wordgame.R
import com.minutesock.wordgame.uiutils.UiText
import com.minutesock.wordgame.utils.FileUtil
import com.minutesock.wordgame.utils.convertToStringList
import java.util.Calendar
import kotlin.random.Random

object GuessWordValidator {
    private var validWords = emptyList<String>()
    private var wordSelection = emptyList<String>()

    // todo extract string resources
    private val failureMessages = listOf(
        "The word eludes you this time.",
        "A valiant effort, but not the word.",
        "The word remains a mystery.",
        "The word stays hidden.",
        "The word is a sly one.",
        "Once more, the word dances out of your sight.",
        "The word prefers to remain in the shadows for now."
    ).shuffled()

    private val encouragingMessages = listOf(
        "Not quite!",
        "The right word is out there!",
        "You're learning from each attempt!",
        "Keep the guesses flowing.",
        "Success is built on persistence.",
        "Embrace the challenge.",
        "Mistakes fuel learning.",
        "Guess by guess!",
        "Closer with each attempt!",
        "Resilience: your compass to word victory!",
        "You're a tenacious one."
    ).shuffled()

    private val correctMessages = listOf(
        "Well guessed!",
        "Marvelous.",
        "Exceptional.",
        "Champion.",
        "You cracked it!",
        "Absolutely right.",
        "Astonishing.",
        "Glorious!",
        "On the nose!"
    ).shuffled()

    private var failureMessageIndex = 0
    private var encouragingMessageIndex = 0
    private var correctMessagesIndex = 0

    fun initValidWords(context: Context) {
        val wordSelectionJsonArray = FileUtil().obtainJsonArray(context, "word_selection.json")
        wordSelectionJsonArray?.let {
            wordSelection = it.convertToStringList()
        }
        val validWordsJsonArray = FileUtil().obtainJsonArray(context, "valid_words.json")
        validWordsJsonArray?.let {
            validWords = it.convertToStringList()
        }
    }

    fun obtainRandomWord(): String {
        return wordSelection[Random(
            Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        ).nextInt(from = 0, until = wordSelection.size)]
    }

    fun validateGuess(
        userGuessWord: UserGuessWord,
        correctWord: String,
        isFinalGuess: Boolean,
    ): DailyWordValidationResult {
        if (userGuessWord.isIncomplete) {
            return DailyWordValidationResult(
                DailyWordValidationResultType.Error,
                UiText.StringResource(R.string.word_is_incomplete)
            )
        }

        if (userGuessWord.word == correctWord) {
            val randomMessageResult = getRandomMessage(correctMessages, correctMessagesIndex)
            correctMessagesIndex = randomMessageResult.second
            return DailyWordValidationResult(
                DailyWordValidationResultType.Success,
                UiText.DynamicString(randomMessageResult.first)
            )
        }

        if (!validWords.contains(userGuessWord.word)) {
            return DailyWordValidationResult(
                DailyWordValidationResultType.Error,
                UiText.StringResource(R.string.word_does_not_exist)
            )
        }
        if (userGuessWord.word != correctWord && validWords.contains(userGuessWord.displayWord.lowercase())) {
            val randomMessageResult = getRandomMessage(
                if (isFinalGuess) failureMessages else encouragingMessages,
                if (isFinalGuess) failureMessageIndex else encouragingMessageIndex
            )
            if (isFinalGuess) {
                failureMessageIndex = randomMessageResult.second
            } else {
                encouragingMessageIndex = randomMessageResult.second
            }

            return DailyWordValidationResult(
                DailyWordValidationResultType.Incorrect,
                UiText.DynamicString(
                    randomMessageResult.first
                )
            )
        }

        return DailyWordValidationResult(
            DailyWordValidationResultType.Unknown,
            UiText.StringResource(R.string.unknown_error)
        )
    }

    private fun getRandomMessage(messagePool: List<String>, index: Int): Pair<String, Int> {
        val i = if (index + 1 == messagePool.size) 0 else index + 1
        return Pair(messagePool[i], i)
    }
}

data class DailyWordValidationResult(
    val type: DailyWordValidationResultType,
    val uiText: UiText
)

enum class DailyWordValidationResultType {
    Unknown,
    Error,
    Incorrect,
    Success
}