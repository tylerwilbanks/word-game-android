package com.minutesock.wordgame.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import com.minutesock.wordgame.domain.UserGuessWord


@Composable
fun DailyWordScreen(
    state: DailyWordState,
    userGuessWords: SnapshotStateList<UserGuessWord>,
    onGameEvent: (DailyWordEventGame) -> Unit,
    onStatsEvent: (DailyWordEventStats) -> Unit
) {

    val bgBlur by remember(state.screenState) {
        mutableStateOf(if (state.screenState == DailyWordScreenState.Stats) 15.dp else 0.dp)
    }

    val visibleStats by remember(state.screenState) {
        mutableStateOf(state.screenState == DailyWordScreenState.Stats)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        DailyWordScreenGame(
            state = state,
            userGuessWords = userGuessWords,
            onEvent = onGameEvent,
            modifier = Modifier.blur(bgBlur)
        )

        AnimatedVisibility(visible = visibleStats) {
            DailyWordScreenStats(
                state = state,
                onEvent = onStatsEvent,
                hasBackgroundScreen = true
            )
        }
    }
}