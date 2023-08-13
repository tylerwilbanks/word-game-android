package com.minutesock.wordgame.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.minutesock.wordgame.uiutils.ShakeConfig
import com.minutesock.wordgame.uiutils.rememberShakeController
import com.minutesock.wordgame.uiutils.shake


@Composable
fun DailyWordScreen(
    state: DailyWordState,
    guessWords: SnapshotStateList<GuessWord>,
    onEvent: (DailyWordEvent) -> Unit,
    falseKeyboardKeys: FalseKeyboardKeys
) {
    val message by remember { mutableStateOf(state.message) }
    val shakeController = rememberShakeController()

    LaunchedEffect(state.message) {
        shakeController.shake(
            ShakeConfig(
                iterations = 2,
                intensity = 2_000f,
                rotateY = 15f,
                translateX = 40f,
            )
        )
    }

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
                    .shake(shakeController)
                    .padding(top = 20.dp, bottom = 15.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                text = state.message,
            )


            guessWords.forEach {
                WordRow(guessWord = it, guessLetters = it.letters)
            }
        }

        FalseKeyboard(falseKeyboardKeys = falseKeyboardKeys, onEvent = onEvent)
    }
}