package com.minutesock.wordgame.domain

import android.content.Context
import com.minutesock.wordgame.R
import com.minutesock.wordgame.utils.FileUtil
import com.minutesock.wordgame.utils.convertToStringList
import java.util.Random

object GuessWordValidator {
    private var validWords = emptyList<String>()
    private var wordSelection = emptyList<String>()
    private val encouragingMessages = listOf("So close!", "Not quite!")

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
        context: Context,
        guessWord: GuessWord,
        correctWord: String
    ): DailyWordValidationResult {
        if (guessWord.isIncomplete) {
            return DailyWordValidationResult(
                DailyWordValidationResultType.Error,
                context.getString(R.string.word_is_incomplete)
            )
        }

        if (guessWord.word == correctWord.lowercase()) {
            return DailyWordValidationResult(DailyWordValidationResultType.Success, "You are correct!")
        }

        if (!validWords.contains(guessWord.word.lowercase())) {
            return DailyWordValidationResult(
                DailyWordValidationResultType.Error,
                context.getString(R.string.word_does_not_exist)
            )
        } else if (validWords.contains(guessWord.word.lowercase())) {
            return DailyWordValidationResult(DailyWordValidationResultType.Incorrect,
                encouragingMessages[Random().nextInt(encouragingMessages.size)]
            )
        }

        return DailyWordValidationResult(DailyWordValidationResultType.Success, "Incorrect word.")
    }
}

data class DailyWordValidationResult(
    val type: DailyWordValidationResultType,
    val message: String
)

enum class DailyWordValidationResultType {
    Error,
    Incorrect,
    Success
}