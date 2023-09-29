package com.minutesock.infinity.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minutesock.core.domain.WordGameMode
import com.minutesock.core.domain.WordScreenState
import com.minutesock.core.presentation.WordGameNotStartedScreen
import com.minutesock.core.presentation.WordGameScreen

@Composable
internal fun InfinityScreen(
    modifier: Modifier = Modifier,
    viewModel: InfinityWordViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val visibleNotStartedScreen by remember(state.screenState) {
        mutableStateOf(
            state.screenState == WordScreenState.NotStarted
        )
    }

    val visibleGameScreen by remember(state.screenState) {
        mutableStateOf(
            state.screenState == WordScreenState.Game
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
            statsContent = {
                InfinityStatsScreen(
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
        exit = fadeOut(),
    ) {
        WordGameNotStartedScreen(
            modifier = modifier,
            gameMode = WordGameMode.Inifinity,
            onEvent = viewModel::onWordGameNotStartedEvent
        )
    }

}