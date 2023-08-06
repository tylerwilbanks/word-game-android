package com.minutesock.wordgame.domain

import androidx.compose.ui.graphics.Color

data class Letter(private val character: Char? = null, val state: LetterState = LetterState.Incorrect) {
    val displayCharacter get() = character?.toString()?.uppercase() ?: ""
    val color get() = when (state) {
        LetterState.Incorrect -> Color.Black
        LetterState.IncorrectPosition -> Color.Yellow
        LetterState.Correct -> Color.Green
    }
}