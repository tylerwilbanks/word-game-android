package com.minutesock.daily.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minutesock.core.domain.WordGameMode
import com.minutesock.core.domain.WordScreenState
import com.minutesock.core.presentation.WordGameNotStartedScreen
import com.minutesock.core.presentation.WordGameScreen
import presentation.DailyWordStatsScreen

@Composable
internal fun DailyWordGameScreen(
    modifier: Modifier = Modifier,
    viewModel: DailyWordViewModel = viewModel(),
    isDarkTheme: Boolean,
    onDarkThemeToggled: (Boolean) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val visibleNotStartedScreen by remember(state.screenState) {
        mutableStateOf(
            state.screenState == WordScreenState.NotStarted
        )
    }

    val visibleGameScreen by remember(state.screenState) {
        mutableStateOf(
            state.screenState == WordScreenState.Game || state.screenState == WordScreenState.Stats
        )
    }

    AnimatedVisibility(
        visible = visibleGameScreen,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        WordGameScreen(
            state = state,
            modifier = modifier,
            onGameEvent = viewModel::onGameEvent,
            isDarkTheme = isDarkTheme,
            onDarkThemeToggled = onDarkThemeToggled,
            statsContent = {
                DailyWordStatsScreen(
                    state = state,
                    onEvent = viewModel::onStatEvent,
                    hasBackgroundScreen = true
                )
            }
        )
    }

    AnimatedVisibility(
        visible = visibleNotStartedScreen,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        WordGameNotStartedScreen(
            modifier = modifier,
            gameMode = WordGameMode.Daily,
            onEvent = viewModel::onWordGameNotStartedEvent
        )
    }
}