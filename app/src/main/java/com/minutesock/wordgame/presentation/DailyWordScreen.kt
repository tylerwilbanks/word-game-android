package com.minutesock.wordgame.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
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
    val shakeController = rememberShakeController()
    val messageColor by animateColorAsState(
        targetValue = if (state.dailyWordStateMessage?.isError == true) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        animationSpec = tween(
            if (state.dailyWordStateMessage?.isError == true) 200 else 1000,
            easing = LinearEasing
        )
    )

    LaunchedEffect(state.dailyWordStateMessage) {
        state.dailyWordStateMessage?.let {
            if (it.isError) {
                shakeController.shake(
                    ShakeConfig.no(1000L) { onEvent(DailyWordEvent.OnErrorAnimationFinished) }
                )
            } else {
                shakeController.shake(
                    ShakeConfig(
                        iterations = 1,
                        intensity = 1_000f,
                        rotateX = 5f,
                        translateY = 15f,
                    )
                )
            }
        }
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
                    .padding(top = 20.dp, bottom = 15.dp, start = 10.dp, end = 10.dp)
                    .animateContentSize(),

                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                text = state.dailyWordStateMessage?.uiText?.asString() ?: "",
                color = messageColor
            )


            guessWords.forEach {
                WordRow(guessWord = it, guessLetters = it.letters, onEvent = onEvent)
            }
        }

        FalseKeyboard(
            falseKeyboardKeys = falseKeyboardKeys,
            onEvent = onEvent,
            guessKeys = state.falseKeyboardKeys
        )
    }
}