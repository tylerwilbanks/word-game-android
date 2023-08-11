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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.minutesock.wordgame.R
import com.minutesock.wordgame.domain.GuessLetter
import com.minutesock.wordgame.presentation.components.FalseKeyboardLetter
import com.minutesock.wordgame.presentation.components.WordRow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList


@Immutable
data class FalseKeyboardKeys(
    val row1: ImmutableList<String> = persistentListOf(
        "q",
        "w",
        "e",
        "r",
        "t",
        "y",
        "u",
        "i",
        "o",
        "p"
    ),
    val row2: ImmutableList<String> = persistentListOf("a", "s", "d", "f", "g", "h", "j", "k", "l"),
    val row3: ImmutableList<String> = persistentListOf(
        "enter",
        "z",
        "x",
        "c",
        "v",
        "b",
        "n",
        "m",
        "remove"
    ),
)

@Composable
fun DailyWordScreen(
    state: State<DailyWordState>,
    onEvent: (DailyWordEvent) -> Unit,
    falseKeyboardKeys: FalseKeyboardKeys
) {
    val animationDuration by remember { mutableStateOf(500) }
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
                text = state.value.message ?: stringResource(id = R.string.what_in_da_word),
            )

            // previous guessed rows
            state.value.previousGuesses.forEach {
                WordRow(guessLetters = it.letters)
            }

            // current guess row
            WordRow(
                guessLetters = state.value.currentGuess.plus(
                    List(state.value.wordLength - state.value.currentGuess.size) {
                        GuessLetter()
                    }
                ).toImmutableList()
            )

            // future guess rows - previous guess rows - 1 for current guess row
            for (i in 0 until state.value.maxGuessAttempts - state.value.previousGuesses.size - 1) {
                WordRow(
                    guessLetters = List(state.value.wordLength)
                    {
                        GuessLetter()
                    }.toImmutableList()
                )
            }
        }

        FalseKeyboard(falseKeyboardKeys = falseKeyboardKeys, onEvent = onEvent)
    }
}

@Composable
fun FalseKeyboard(
    falseKeyboardKeys: FalseKeyboardKeys,
    onEvent: (DailyWordEvent) -> Unit
) {
    Column(
        modifier = Modifier.padding(bottom = 20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row {
            falseKeyboardKeys.row1.forEach {
                FalseKeyboardLetter(onEvent = onEvent, displayText = it)
            }
        }
        Row {
            falseKeyboardKeys.row2.forEach {
                FalseKeyboardLetter(onEvent = onEvent, displayText = it)
            }
        }
        Row {
            falseKeyboardKeys.row3.forEach {
                FalseKeyboardLetter(onEvent = onEvent, displayText = it)
            }
        }
    }
}