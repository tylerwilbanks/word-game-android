package com.minutesock.core.presentation

import com.minutesock.core.domain.GuessKeyboardLetter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FalseKeyboardKeys(
    val row1: ImmutableList<GuessKeyboardLetter> = persistentListOf(
        GuessKeyboardLetter("q"),
        GuessKeyboardLetter("w"),
        GuessKeyboardLetter("e"),
        GuessKeyboardLetter("r"),
        GuessKeyboardLetter("t"),
        GuessKeyboardLetter("y"),
        GuessKeyboardLetter("u"),
        GuessKeyboardLetter("i"),
        GuessKeyboardLetter("o"),
        GuessKeyboardLetter("p")
    ),
    val row2: ImmutableList<GuessKeyboardLetter> = persistentListOf(
        GuessKeyboardLetter("a"),
        GuessKeyboardLetter("s"),
        GuessKeyboardLetter("d"),
        GuessKeyboardLetter("f"),
        GuessKeyboardLetter("g"),
        GuessKeyboardLetter("h"),
        GuessKeyboardLetter("j"),
        GuessKeyboardLetter("k"),
        GuessKeyboardLetter("l"),
    ),
    val row3: ImmutableList<GuessKeyboardLetter> = persistentListOf(
        GuessKeyboardLetter("enter"),
        GuessKeyboardLetter("z"),
        GuessKeyboardLetter("x"),
        GuessKeyboardLetter("c"),
        GuessKeyboardLetter("v"),
        GuessKeyboardLetter("b"),
        GuessKeyboardLetter("n"),
        GuessKeyboardLetter("m"),
        GuessKeyboardLetter("remove"),
    )
)