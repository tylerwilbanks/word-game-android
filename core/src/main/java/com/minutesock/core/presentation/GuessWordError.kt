package com.minutesock.core.presentation

import com.minutesock.core.R
import com.minutesock.core.uiutils.UiText

enum class GuessWordError {
    None,
    Unknown,
    NoWordToEdit,
    NoLettersAvailableForInput,
    NoLettersToRemove;

    val message: UiText
        get() {
            return when (this) {
                None -> UiText.DynamicString("")
                Unknown -> UiText.StringResource(R.string.unknown_error)
                NoWordToEdit -> UiText.StringResource(R.string.there_are_no_words_to_edit)
                NoLettersAvailableForInput -> UiText.StringResource(R.string.this_word_is_full)
                NoLettersToRemove -> UiText.StringResource(R.string.this_word_is_empty)
            }
        }
}