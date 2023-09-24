package com.minutesock.dictionary.domain

import androidx.navigation.NavController

sealed class DictionaryEvent {
    data class OnWordInfoListItemClicked(val navController: NavController, val word: String) : DictionaryEvent()
}
