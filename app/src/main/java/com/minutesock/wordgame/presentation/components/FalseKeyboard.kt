package com.minutesock.wordgame.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minutesock.wordgame.domain.GuessKey
import com.minutesock.wordgame.presentation.DailyWordEventGame
import com.minutesock.wordgame.presentation.FalseKeyboardKeys
import kotlinx.collections.immutable.ImmutableList

@Composable
fun FalseKeyboard(
    falseKeyboardKeys: FalseKeyboardKeys,
    onEvent: (DailyWordEventGame) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(bottom = 20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FalseKeyboardRow(row = falseKeyboardKeys.row1, onEvent = onEvent)
        FalseKeyboardRow(row = falseKeyboardKeys.row2, onEvent = onEvent)
        FalseKeyboardRow(row = falseKeyboardKeys.row3, onEvent = onEvent)
    }
}

@Composable
fun FalseKeyboardRow(row: ImmutableList<GuessKey>, onEvent: (DailyWordEventGame) -> Unit) {
    Row {
        row.forEach {
            FalseKeyboardLetter(onEvent = onEvent, guessKey = it)
        }
    }
}