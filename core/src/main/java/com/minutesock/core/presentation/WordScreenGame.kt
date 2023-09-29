package com.minutesock.core.presentation

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
import androidx.compose.foundation.layout.size
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
import com.minutesock.core.domain.DailyWordState
import com.minutesock.core.domain.GuessWord
import com.minutesock.core.domain.WordEventGame
import com.minutesock.core.domain.WordGameMode
import com.minutesock.core.domain.WordGameState
import com.minutesock.core.presentation.components.FalseKeyboard
import com.minutesock.core.presentation.components.WordRow
import com.minutesock.core.uiutils.ShakeConfig
import com.minutesock.core.uiutils.rememberShakeController
import com.minutesock.core.uiutils.shake
import kotlinx.collections.immutable.ImmutableList


@Composable
fun WordScreenGame(
    state: DailyWordState,
    guessWords: ImmutableList<GuessWord>,
    onEvent: (WordEventGame) -> Unit,
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
                        state.gameState == WordGameState.Failure)
            ) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            animationSpec = tween(
                if (state.dailyWordStateMessage?.isError == true) 200 else 1000,
                easing = LinearEasing
            ), label = "title message color"
        )

        LaunchedEffect(state.dailyWordStateMessage) {
            if (state.wordRowAnimating) {
                return@LaunchedEffect
            }
            state.dailyWordStateMessage?.let {
                if (it.isError || state.gameState == WordGameState.Failure) {
                    shakeController.shake(
                        ShakeConfig.no(defaultMessageDelay) {
                            if (state.gameState == WordGameState.Failure) {
                                onEvent(WordEventGame.OnCompleteAnimationFinished)
                            } else {
                                onEvent(WordEventGame.OnErrorAnimationFinished)
                            }
                        }
                    )
                    return@LaunchedEffect
                }
            }
            if (state.gameState == WordGameState.Success) {
                shakeController.shake(
                    ShakeConfig.yes(defaultMessageDelay) {
                        onEvent(
                            WordEventGame.OnCompleteAnimationFinished
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

        val gameModeIconId by remember(state.gameMode) {
            mutableStateOf(
                when (state.gameMode) {
                    WordGameMode.Daily -> R.drawable.baseline_today_24
                    WordGameMode.Inifinity -> R.drawable.baseline_infinity
                }
            )
        }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    modifier = Modifier
                        .size(45.dp)
                        .padding(10.dp),
                    painter = painterResource(
                        id = gameModeIconId
                    ),
                    contentDescription = null
                )
                IconButton(onClick = { onEvent(WordEventGame.OnStatsPress) }) {
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
