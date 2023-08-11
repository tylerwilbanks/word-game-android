package com.minutesock.wordgame.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minutesock.wordgame.domain.GuessLetter
import kotlinx.collections.immutable.ImmutableList

@Composable
fun WordRow(
    guessLetters: ImmutableList<GuessLetter>
) {
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        guessLetters.forEach {
            LetterCard(letter = it)
        }
    }
}