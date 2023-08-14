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
import com.minutesock.wordgame.presentation.DailyWordEvent
import com.minutesock.wordgame.presentation.FalseKeyboardKeys
import kotlinx.collections.immutable.ImmutableList

@Composable
fun FalseKeyboard(
    falseKeyboardKeys: FalseKeyboardKeys,
    onEvent: (DailyWordEvent) -> Unit,
    guessKeys: ImmutableList<GuessKey>
) {
    Column(
        modifier = Modifier.padding(bottom = 20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row {
            falseKeyboardKeys.row1.forEach {
                FalseKeyboardLetter(onEvent = onEvent, displayText = it, guessKeys = guessKeys)
            }
        }

        Row {
            falseKeyboardKeys.row2.forEach {
                FalseKeyboardLetter(onEvent = onEvent, displayText = it, guessKeys = guessKeys)
            }
        }


        Row {
            falseKeyboardKeys.row3.forEach {
                FalseKeyboardLetter(onEvent = onEvent, displayText = it, guessKeys = guessKeys)
            }
        }
    }
}