package com.minutesock.dictionary.domain

sealed class DictionaryEvent{
    data class OnWordInfoListItemClicked(val word: String) : DictionaryEvent()
}
