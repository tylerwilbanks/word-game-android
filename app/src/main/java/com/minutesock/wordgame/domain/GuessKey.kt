package com.minutesock.wordgame.domain

import androidx.compose.ui.graphics.Color
import com.minutesock.wordgame.ui.theme.guessLetterGreen
import com.minutesock.wordgame.ui.theme.guessLetterYellow

data class GuessKey(
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