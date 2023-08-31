package com.minutesock.daily.domain

import androidx.compose.ui.graphics.Color
import com.minutesock.core.theme.guessLetterGreen
import com.minutesock.core.theme.guessLetterYellow

data class UserGuessLetter(
    private val _character: Char = AvailableChar,
    val state: UserLetterState = UserLetterState.Absent
) {
    val displayCharacter get() = character.toString().uppercase()
    val character get() = _character.lowercaseChar()

    fun displayColor(absentBackgroundColor: Color) = when (state) {
        UserLetterState.Unknown -> absentBackgroundColor
        UserLetterState.Absent -> absentBackgroundColor
        UserLetterState.Present -> guessLetterYellow
        UserLetterState.Correct -> guessLetterGreen
    }

    val availableForInput get() = character == AvailableChar

    val answered get() = !availableForInput

    companion object {
        const val AvailableChar = ' '
    }
}

fun UserGuessLetter.erase(): UserGuessLetter {
    return this.copy(
        _character = UserGuessLetter.AvailableChar
    )
}