package com.minutesock.daily.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minutesock.core.presentation.DailyWordStatsScreen
import com.minutesock.core.presentation.WordGameScreen

@Composable
internal fun DailyWordGameScreen(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    viewModel: DailyWordViewModel = viewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.setupGame()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    WordGameScreen(
        state = state,
        modifier = modifier,
        onGameEvent = viewModel::onGameEvent,
        statsContent = {
            DailyWordStatsScreen(
                state = state,
                onEvent = viewModel::onStatEvent,
                hasBackgroundScreen = true
            )
        }
    )
}