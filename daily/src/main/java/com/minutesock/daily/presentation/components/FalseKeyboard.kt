package com.minutesock.daily.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minutesock.core.domain.GuessKeyboardLetter
import com.minutesock.core.presentation.FalseKeyboardKeys
import com.minutesock.daily.presentation.DailyWordEventGame
import kotlinx.collections.immutable.ImmutableList

@Composable
fun FalseKeyboard(
    falseKeyboardKeys: FalseKeyboardKeys,
    onEvent: (DailyWordEventGame) -> Unit,
    modifier: Modifier = Modifier,
    isWordRowAnimating: Boolean = false,
) {
    Column(
        modifier = modifier.padding(bottom = 20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FalseKeyboardRow(row = falseKeyboardKeys.row1, onEvent = onEvent)
        FalseKeyboardRow(row = falseKeyboardKeys.row2, onEvent = onEvent)
        FalseKeyboardRow(
            row = falseKeyboardKeys.row3,
            onEvent = onEvent,
            isWordRowAnimating = isWordRowAnimating
        )
    }
}

@Composable
fun FalseKeyboardRow(
    row: ImmutableList<GuessKeyboardLetter>,
    onEvent: (DailyWordEventGame) -> Unit,
    isWordRowAnimating: Boolean = false
) {
    Row {
        row.forEach {
            FalseKeyboardLetter(
                onEvent = onEvent,
                guessKeyboardLetter = it,
                isWordRowAnimating = isWordRowAnimating
            )
        }
    }
}