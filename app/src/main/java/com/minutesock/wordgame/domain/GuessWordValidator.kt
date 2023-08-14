package com.minutesock.wordgame.domain

import android.content.Context
import com.minutesock.wordgame.R
import com.minutesock.wordgame.uiutils.UiText
import com.minutesock.wordgame.utils.FileUtil
import com.minutesock.wordgame.utils.convertToStringList
import java.util.Random

object GuessWordValidator {
    private var validWords = emptyList<String>()
    private var wordSelection = emptyList<String>()

    // todo extract string resources
    private val encouragingMessages = listOf("So close!", "Not quite!")
    private val correctMessages = listOf("You are correct!", "Marvelous.", "Exceptional.")

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

    fun validateGuess(
        guessWord: GuessWord,
        correctWord: String
    ): DailyWordValidationResult {
        if (guessWord.isIncomplete) {
            return DailyWordValidationResult(
                DailyWordValidationResultType.Error,
                UiText.StringResource(R.string.word_is_incomplete)
            )
        }

        if (guessWord.word == correctWord) {
            return DailyWordValidationResult(
                DailyWordValidationResultType.Success,
                UiText.DynamicString(correctMessages[Random().nextInt(correctMessages.size)])
            )
        }

        if (!validWords.contains(guessWord.word)) {
            return DailyWordValidationResult(
                DailyWordValidationResultType.Error,
                UiText.StringResource(R.string.word_does_not_exist)
            )
        }
        if (guessWord.word != correctWord && validWords.contains(guessWord.displayWord.lowercase())) {
            return DailyWordValidationResult(
                DailyWordValidationResultType.Incorrect,
                UiText.DynamicString(encouragingMessages[Random().nextInt(encouragingMessages.size)])
            )
        }

        return DailyWordValidationResult(
            DailyWordValidationResultType.Unknown,
            UiText.StringResource(R.string.unknown_error)
        )
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