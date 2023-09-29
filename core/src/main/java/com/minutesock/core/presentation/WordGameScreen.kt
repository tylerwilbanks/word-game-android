package com.minutesock.core.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import com.minutesock.core.domain.DailyWordState
import com.minutesock.core.domain.WordEventGame
import com.minutesock.core.domain.WordScreenState


@Composable
fun WordGameScreen(
    modifier: Modifier = Modifier,
    state: DailyWordState,
    onGameEvent: (WordEventGame) -> Unit,
    statsContent: @Composable() () -> Unit,
) {

    val bgBlur by remember(state.screenState) {
        mutableStateOf(if (state.screenState == WordScreenState.Stats) 15.dp else 0.dp)
    }

    val visibleStats by remember(state.screenState) {
        mutableStateOf(state.screenState == WordScreenState.Stats)
    }



    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        WordScreenGame(
            state = state,
            guessWords = state.guessWords,
            onEvent = onGameEvent,
            modifier = Modifier.blur(bgBlur)
        )

        AnimatedVisibility(visible = visibleStats) {
            statsContent()
        }
    }
}