package com.minutesock.core.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minutesock.core.domain.DailyWordScreenState
import com.minutesock.core.domain.WordGameMode


@Composable
fun WordGameScreen(
    gameMode: WordGameMode,
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    wordGameViewModel: WordGameViewModel = viewModel()
) {

    val state by wordGameViewModel.state.collectAsStateWithLifecycle()
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                wordGameViewModel.setupGame(gameMode)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    val bgBlur by remember(state.screenState) {
        mutableStateOf(if (state.screenState == DailyWordScreenState.Stats) 15.dp else 0.dp)
    }

    val visibleStats by remember(state.screenState) {
        mutableStateOf(state.screenState == DailyWordScreenState.Stats)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        WordScreenGame(
            state = state,
            guessWords = state.guessWords,
            onEvent = wordGameViewModel::onGameEvent,
            modifier = Modifier.blur(bgBlur)
        )

        AnimatedVisibility(visible = visibleStats) {
            WordScreenStats(
                state = state,
                onEvent = wordGameViewModel::onStatsEvent,
                hasBackgroundScreen = true
            )
        }
    }
}