package com.minutesock.core.domain

import androidx.compose.ui.graphics.Color
import com.minutesock.core.theme.guessLetterGreen
import com.minutesock.core.theme.guessLetterYellow

data class GuessLetter(
    private val _character: Char = AvailableChar,
    val state: LetterState = LetterState.Absent
) {
    val displayCharacter get() = character.toString().uppercase()
    val character get() = _character.lowercaseChar()

    fun displayColor(absentBackgroundColor: Color) = when (state) {
        LetterState.Unknown -> absentBackgroundColor
        LetterState.Absent -> absentBackgroundColor
        LetterState.Present -> guessLetterYellow
        LetterState.Correct -> guessLetterGreen
    }

    val availableForInput get() = character == AvailableChar

    val answered get() = !availableForInput

    companion object {
        const val AvailableChar = ' '
    }
}

fun GuessLetter.erase(): GuessLetter {
    return this.copy(
        _character = GuessLetter.AvailableChar
    )
}