package com.minutesock.daily.presentation

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FalseKeyboardKeys(
    val row1: ImmutableList<com.minutesock.daily.domain.UserGuessKey> = persistentListOf(
        com.minutesock.daily.domain.UserGuessKey("q"),
        com.minutesock.daily.domain.UserGuessKey("w"),
        com.minutesock.daily.domain.UserGuessKey("e"),
        com.minutesock.daily.domain.UserGuessKey("r"),
        com.minutesock.daily.domain.UserGuessKey("t"),
        com.minutesock.daily.domain.UserGuessKey("y"),
        com.minutesock.daily.domain.UserGuessKey("u"),
        com.minutesock.daily.domain.UserGuessKey("i"),
        com.minutesock.daily.domain.UserGuessKey("o"),
        com.minutesock.daily.domain.UserGuessKey("p")
    ),
    val row2: ImmutableList<com.minutesock.daily.domain.UserGuessKey> = persistentListOf(
        com.minutesock.daily.domain.UserGuessKey("a"),
        com.minutesock.daily.domain.UserGuessKey("s"),
        com.minutesock.daily.domain.UserGuessKey("d"),
        com.minutesock.daily.domain.UserGuessKey("f"),
        com.minutesock.daily.domain.UserGuessKey("g"),
        com.minutesock.daily.domain.UserGuessKey("h"),
        com.minutesock.daily.domain.UserGuessKey("j"),
        com.minutesock.daily.domain.UserGuessKey("k"),
        com.minutesock.daily.domain.UserGuessKey("l"),
    ),
    val row3: ImmutableList<com.minutesock.daily.domain.UserGuessKey> = persistentListOf(
        com.minutesock.daily.domain.UserGuessKey("enter"),
        com.minutesock.daily.domain.UserGuessKey("z"),
        com.minutesock.daily.domain.UserGuessKey("x"),
        com.minutesock.daily.domain.UserGuessKey("c"),
        com.minutesock.daily.domain.UserGuessKey("v"),
        com.minutesock.daily.domain.UserGuessKey("b"),
        com.minutesock.daily.domain.UserGuessKey("n"),
        com.minutesock.daily.domain.UserGuessKey("m"),
        com.minutesock.daily.domain.UserGuessKey("remove"),
    )
)