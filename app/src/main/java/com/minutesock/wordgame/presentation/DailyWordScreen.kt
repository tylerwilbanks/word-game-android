package com.minutesock.wordgame.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.minutesock.wordgame.R
import com.minutesock.wordgame.presentation.components.FalseKeyboardLetter
import com.minutesock.wordgame.presentation.components.WordRow

@Composable
fun DailyWordScreen(
    state: DailyWordState,
    onEvent: (DailyWordEvent) -> Unit
) {
    val animationDuration = 500
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 15.dp)
                    .animateContentSize(
                        animationSpec = tween(animationDuration)
                    ),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                text = state.message ?: stringResource(id = R.string.what_in_da_word),
            )

            state.guesses.forEach {
                WordRow(state = state, guess = it)
            }
        }

        Column(
            modifier = Modifier.padding(bottom = 20.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val keyboardRows = listOf(
                listOf(
                    "q", "w", "e", "r", "t", "y", "u", "i", "o", "p",
                ),
                listOf(
                    "a", "s", "d", "f", "g", "h", "j", "k", "l",
                ),
                listOf(
                    "enter", "z", "x", "c", "v", "b", "n", "m", "remove"
                )
            )

            keyboardRows.forEach { keyboardRow ->
                Row {
                    keyboardRow.forEach {
                        FalseKeyboardLetter(state = state, displayText = it, onEvent = onEvent)
                    }
                }
            }
        }
    }
}