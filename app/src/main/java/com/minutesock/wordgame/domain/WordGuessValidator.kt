package com.minutesock.wordgame.domain

import android.content.Context
import com.minutesock.wordgame.R
import com.minutesock.wordgame.utils.FileUtil
import com.minutesock.wordgame.utils.convertToStringList

object WordGuessValidator {
    private var validWords = emptyList<String>()

    fun initValidWords(context: Context) {
        val jsonArray = FileUtil().obtainJsonArray(context, "valid_words.json")
        jsonArray?.let {
            validWords = it.convertToStringList()
        }
    }

    fun validateGuess(context: Context, wordGuess: WordGuess, correctWord: String): ValidationResult {
        if (wordGuess.isIncomplete) {
            return ValidationResult(ValidationResultType.Error, context.getString(R.string.word_is_incomplete))
        }
        if (!validWords.contains(wordGuess.word)) {
            ValidationResult(ValidationResultType.Error, context.getString(R.string.word_does_not_exist))
        }

        if (wordGuess.word == correctWord.uppercase()) {
            return ValidationResult(ValidationResultType.Success, "You are correct!")
        }

        return ValidationResult(ValidationResultType.UnknownError, "Something went wrong.")
    }

    data class ValidationResult(
        val type: ValidationResultType,
        val message: String
    )

    enum class ValidationResultType {
        Error,
        UnknownError,
        Success
    }
}