package com.minutesock.wordgame.domain

import android.content.Context
import com.minutesock.wordgame.R
import com.minutesock.wordgame.utils.FileUtil
import com.minutesock.wordgame.utils.convertToStringList

object GuessWordValidator {
    private var validWords = emptyList<String>()

    fun initValidWords(context: Context) {
        val jsonArray = FileUtil().obtainJsonArray(context, "valid_words.json")
        jsonArray?.let {
            validWords = it.convertToStringList()
        }
    }

    fun validateGuess(
        context: Context,
        guessWord: GuessWord,
        correctWord: String
    ): ValidationResult {
        if (guessWord.isIncomplete) {
            return ValidationResult(
                ValidationResultType.Error,
                context.getString(R.string.word_is_incomplete)
            )
        }
        if (!validWords.contains(guessWord.word)) {
            return ValidationResult(
                ValidationResultType.Error,
                context.getString(R.string.word_does_not_exist)
            )
        }

        if (guessWord.word == correctWord.uppercase()) {
            return ValidationResult(ValidationResultType.Success, "You are correct!")
        }
        return ValidationResult(ValidationResultType.Success, "Incorrect word.")
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