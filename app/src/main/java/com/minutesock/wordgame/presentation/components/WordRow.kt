package com.minutesock.wordgame.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minutesock.wordgame.domain.GuessLetter
import com.minutesock.wordgame.presentation.DailyWordState

@Composable
fun WordRow(
    letters: List<GuessLetter>,
    wordLength: Int,
    rowNum: Int
) {
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 0 until wordLength) {
            LetterCard(letter = letters.getOrNull(i + rowNum * wordLength))
        }
    }
}