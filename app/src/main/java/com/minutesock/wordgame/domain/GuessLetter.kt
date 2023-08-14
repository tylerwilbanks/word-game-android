package com.minutesock.wordgame.domain

import androidx.compose.ui.graphics.Color
import com.minutesock.wordgame.ui.theme.guessLetterGreen
import com.minutesock.wordgame.ui.theme.guessLetterYellow

data class GuessLetter(
    private val _character: Char = AvailableChar,
    val state: LetterState = LetterState.Absent
) {
    val displayCharacter get() = character.toString().uppercase()
    val character get() = _character.lowercaseChar()

    fun displayColor(absentBackgroundColor: Color) = when (state) {
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