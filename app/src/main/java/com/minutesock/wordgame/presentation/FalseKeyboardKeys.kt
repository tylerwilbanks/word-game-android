package com.minutesock.wordgame.presentation

import com.minutesock.wordgame.domain.UserGuessKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FalseKeyboardKeys(
    val row1: ImmutableList<UserGuessKey> = persistentListOf(
        UserGuessKey("q"),
        UserGuessKey("w"),
        UserGuessKey("e"),
        UserGuessKey("r"),
        UserGuessKey("t"),
        UserGuessKey("y"),
        UserGuessKey("u"),
        UserGuessKey("i"),
        UserGuessKey("o"),
        UserGuessKey("p")
    ),
    val row2: ImmutableList<UserGuessKey> = persistentListOf(
        UserGuessKey("a"),
        UserGuessKey("s"),
        UserGuessKey("d"),
        UserGuessKey("f"),
        UserGuessKey("g"),
        UserGuessKey("h"),
        UserGuessKey("j"),
        UserGuessKey("k"),
        UserGuessKey("l"),
    ),
    val row3: ImmutableList<UserGuessKey> = persistentListOf(
        UserGuessKey("enter"),
        UserGuessKey("z"),
        UserGuessKey("x"),
        UserGuessKey("c"),
        UserGuessKey("v"),
        UserGuessKey("b"),
        UserGuessKey("n"),
        UserGuessKey("m"),
        UserGuessKey("remove"),
    )
)