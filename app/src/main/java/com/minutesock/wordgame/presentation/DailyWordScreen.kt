package com.minutesock.wordgame.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.minutesock.wordgame.domain.GuessWord
import com.minutesock.wordgame.presentation.components.FalseKeyboard
import com.minutesock.wordgame.presentation.components.WordRow


@Composable
fun DailyWordScreen(
    state: DailyWordState,
    guessWords: SnapshotStateList<GuessWord>,
    onEvent: (DailyWordEvent) -> Unit,
    falseKeyboardKeys: FalseKeyboardKeys
) {
    val animationDuration by remember { mutableStateOf(500) }
    val message by remember { mutableStateOf(state.message) }
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
                text = state.message,
            )

            guessWords.forEach {
                WordRow(guessLetters = it.letters)
            }
        }

        FalseKeyboard(falseKeyboardKeys = falseKeyboardKeys, onEvent = onEvent)
    }
}