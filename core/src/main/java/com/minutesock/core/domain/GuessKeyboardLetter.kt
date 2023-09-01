package com.minutesock.core.domain

import androidx.compose.ui.graphics.Color
import com.minutesock.core.theme.guessLetterGreen
import com.minutesock.core.theme.guessLetterYellow

data class GuessKeyboardLetter(
    val keyName: String,
    val state: LetterState = LetterState.Unknown,
    val character: Char = keyName.first()
) {
    fun displayColor(defaultColor: Color) = when (state) {
        LetterState.Unknown -> defaultColor
        LetterState.Absent -> defaultColor.copy(alpha = 0.25f)
        LetterState.Present -> guessLetterYellow
        LetterState.Correct -> guessLetterGreen
    }
}