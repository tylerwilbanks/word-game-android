package com.minutesock.wordgame.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import com.minutesock.wordgame.domain.GuessWord


@Composable
fun DailyWordScreen(
    state: DailyWordState,
    guessWords: SnapshotStateList<GuessWord>,
    onEvent: (DailyWordEvent) -> Unit,
) {

    val bgBlur by remember(state.screenState) {
        mutableStateOf(if (state.screenState == DailyWordScreenState.Complete) 15.dp else 0.dp)
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        DailyWordGameScreen(
            state = state,
            guessWords = guessWords,
            onEvent = onEvent,
            modifier = Modifier.blur(bgBlur)
        )

        if (state.screenState == DailyWordScreenState.Complete) {
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.55f)
                    )
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.dailyWordStateMessage?.uiText?.asString() ?: ""
                )
            }
        }
    }
}