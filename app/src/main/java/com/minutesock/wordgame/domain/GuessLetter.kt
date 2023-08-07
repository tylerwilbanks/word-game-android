package com.minutesock.wordgame.domain

import androidx.compose.ui.graphics.Color

class GuessLetter(private var character: Char = ' ', val state: LetterState = LetterState.Absent) {
    val displayCharacter get() = character.toString().uppercase()
    val color
        get() = when (state) {
            LetterState.Absent -> Color.Black
            LetterState.Present -> Color.Yellow
            LetterState.Correct -> Color.Green
        }

    fun updateCharacter(newCharacter: Char) {
        character = newCharacter;
    }

    val availableForInput get() = character == ' '
}