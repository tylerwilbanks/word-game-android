package com.minutesock.daily.presentation

import com.minutesock.daily.R

enum class GuessWordError {
    None,
    Unknown,
    NoWordToEdit,
    NoLettersAvailableForInput,
    NoLettersToRemove;

    val message: com.minutesock.core.uiutils.UiText
        get() {
            return when (this) {
                None -> com.minutesock.core.uiutils.UiText.DynamicString("")
                Unknown -> com.minutesock.core.uiutils.UiText.StringResource(R.string.unknown_error)
                NoWordToEdit -> com.minutesock.core.uiutils.UiText.StringResource(R.string.there_are_no_words_to_edit)
                NoLettersAvailableForInput -> com.minutesock.core.uiutils.UiText.StringResource(R.string.this_word_is_full)
                NoLettersToRemove -> com.minutesock.core.uiutils.UiText.StringResource(R.string.this_word_is_empty)
            }
        }
}