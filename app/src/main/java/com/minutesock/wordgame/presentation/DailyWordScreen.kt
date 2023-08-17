package com.minutesock.wordgame.presentation

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
import com.minutesock.wordgame.domain.GuessWord


@Composable
fun DailyWordScreen(
    state: DailyWordState,
    guessWords: SnapshotStateList<GuessWord>,
    onGameEvent: (DailyWordEventGame) -> Unit,
    onStatsEvent: (DailyWordEventStats) -> Unit
) {

    val bgBlur by remember(state.screenState) {
        mutableStateOf(if (state.screenState == DailyWordScreenState.Stats) 15.dp else 0.dp)
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        DailyWordScreenGame(
            state = state,
            guessWords = guessWords,
            onEvent = onGameEvent,
            modifier = Modifier.blur(bgBlur)
        )

        when (state.screenState) {
            DailyWordScreenState.NotStarted -> {}
            DailyWordScreenState.Stats -> {
                DailyWordScreenStats(
                    state = state,
                    onStatsEvent = onStatsEvent,
                    hasBackgroundScreen = true
                )
            }

            else -> {}
        }
    }
}