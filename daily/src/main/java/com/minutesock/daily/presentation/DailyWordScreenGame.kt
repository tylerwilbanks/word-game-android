package com.minutesock.daily.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.minutesock.core.R
import com.minutesock.core.domain.DailyWordGameState
import com.minutesock.core.domain.DailyWordState
import com.minutesock.core.domain.GuessWord
import com.minutesock.core.uiutils.ShakeConfig
import com.minutesock.core.uiutils.rememberShakeController
import com.minutesock.core.uiutils.shake
import com.minutesock.daily.presentation.components.FalseKeyboard
import com.minutesock.daily.presentation.components.WordRow
import kotlinx.collections.immutable.ImmutableList


@Composable
fun DailyWordScreenGame(
    state: DailyWordState,
    guessWords: ImmutableList<GuessWord>,
    onEvent: (DailyWordEventGame) -> Unit,
    modifier: Modifier = Modifier
) {
    val defaultMessageDelay by remember {
        mutableStateOf(1000L)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {


        val shakeController = rememberShakeController()
        val messageColor by animateColorAsState(
            targetValue = if (
                state.dailyWordStateMessage?.uiText?.asString() != stringResource(id = R.string.what_in_da_word) &&
                (state.dailyWordStateMessage?.isError == true ||
                        state.gameState == DailyWordGameState.Failure)
            ) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            animationSpec = tween(
                if (state.dailyWordStateMessage?.isError == true) 200 else 1000,
                easing = LinearEasing
            ), label = "title message color"
        )

        LaunchedEffect(state.dailyWordStateMessage) {
            state.dailyWordStateMessage?.let {
                if (it.isError || state.gameState == DailyWordGameState.Failure) {
                    shakeController.shake(
                        ShakeConfig.no(defaultMessageDelay) {
                            if (state.gameState == DailyWordGameState.Failure) {
                                onEvent(DailyWordEventGame.OnCompleteAnimationFinished)
                            } else {
                                onEvent(DailyWordEventGame.OnErrorAnimationFinished)
                            }
                        }
                    )
                    return@LaunchedEffect
                }
            }
            if (state.gameState == DailyWordGameState.Success) {
                shakeController.shake(
                    ShakeConfig.yes(defaultMessageDelay) {
                        onEvent(
                            DailyWordEventGame.OnCompleteAnimationFinished
                        )
                    }
                )
                return@LaunchedEffect
            }

            shakeController.shake(
                ShakeConfig(
                    iterations = 1,
                    intensity = 1_000f,
                    rotateX = 5f,
                    translateY = 15f,
                )
            )

        }
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onEvent(DailyWordEventGame.OnStatsPress) }) {
                    Icon(
                        painterResource(id = R.drawable.baseline_bar_chart_24),
                        contentDescription = "Stats"
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .shake(shakeController)
                        .padding(bottom = 15.dp, start = 10.dp, end = 10.dp)
                        .animateContentSize(),

                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    text = state.dailyWordStateMessage?.uiText?.asString() ?: "",
                    color = messageColor
                )
            }

            guessWords.forEach {
                WordRow(
                    guessWord = it,
                    guessLetters = it.letters,
                    message = state.dailyWordStateMessage?.uiText?.asString(),
                    wordRowAnimating = state.wordRowAnimating,
                    onEvent = onEvent
                )
            }
        }

        FalseKeyboard(
            falseKeyboardKeys = state.falseKeyboardKeys,
            onEvent = onEvent,
            modifier = Modifier.fillMaxSize(),
            isWordRowAnimating = state.wordRowAnimating
        )
    }
}
