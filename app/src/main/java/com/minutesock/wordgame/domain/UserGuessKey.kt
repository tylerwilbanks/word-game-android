package com.minutesock.wordgame.domain

import androidx.compose.ui.graphics.Color
import com.minutesock.wordgame.ui.theme.guessLetterGreen
import com.minutesock.wordgame.ui.theme.guessLetterYellow

data class UserGuessKey(
    val keyName: String,
    val state: UserLetterState = UserLetterState.Unknown,
    val character: Char = keyName.first()
) {
    fun displayColor(defaultColor: Color) = when (state) {
        UserLetterState.Unknown -> defaultColor
        UserLetterState.Absent -> defaultColor.copy(alpha = 0.25f)
        UserLetterState.Present -> guessLetterYellow
        UserLetterState.Correct -> guessLetterGreen
    }
}