package com.minutesock.wordgame.presentation

import com.minutesock.wordgame.domain.GuessKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FalseKeyboardKeys(
    val row1: ImmutableList<GuessKey> = persistentListOf(
        GuessKey("q"),
        GuessKey("w"),
        GuessKey("e"),
        GuessKey("r"),
        GuessKey("t"),
        GuessKey("y"),
        GuessKey("u"),
        GuessKey("i"),
        GuessKey("o"),
        GuessKey("p")
    ),
    val row2: ImmutableList<GuessKey> = persistentListOf(
        GuessKey("a"),
        GuessKey("s"),
        GuessKey("d"),
        GuessKey("f"),
        GuessKey("g"),
        GuessKey("h"),
        GuessKey("j"),
        GuessKey("k"),
        GuessKey("l"),
    ),
    val row3: ImmutableList<GuessKey> = persistentListOf(
        GuessKey("enter"),
        GuessKey("z"),
        GuessKey("x"),
        GuessKey("c"),
        GuessKey("v"),
        GuessKey("b"),
        GuessKey("n"),
        GuessKey("m"),
        GuessKey("remove"),
    )
)