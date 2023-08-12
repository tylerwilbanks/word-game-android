package com.minutesock.wordgame.domain

import androidx.compose.ui.graphics.Color

data class GuessLetter(
    private val _character: Char = AvailableChar,
    val state: LetterState = LetterState.Absent
) {
    val displayCharacter get() = character.toString().uppercase()
    val character get() = _character.lowercaseChar()

    val color
        get() = when (state) {
            LetterState.Absent -> Color.Black
            LetterState.Present -> Color.Yellow
            LetterState.Correct -> Color.Green
        }

    val availableForInput get() = character == AvailableChar

    val answered get() = !availableForInput

    companion object {
        const val AvailableChar = ' '
    }
}

fun GuessLetter.updateState() {
    // todo implement
}

fun GuessLetter.erase(): GuessLetter {
    return this.copy(
        _character = GuessLetter.AvailableChar
    )
}