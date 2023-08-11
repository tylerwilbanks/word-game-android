package com.minutesock.wordgame.presentation

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class FalseKeyboardKeys(
    val row1: ImmutableList<String> = persistentListOf(
        "q",
        "w",
        "e",
        "r",
        "t",
        "y",
        "u",
        "i",
        "o",
        "p"
    ),
    val row2: ImmutableList<String> = persistentListOf("a", "s", "d", "f", "g", "h", "j", "k", "l"),
    val row3: ImmutableList<String> = persistentListOf(
        "enter",
        "z",
        "x",
        "c",
        "v",
        "b",
        "n",
        "m",
        "remove"
    ),
)