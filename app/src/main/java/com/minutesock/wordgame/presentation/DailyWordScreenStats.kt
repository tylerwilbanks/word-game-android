package com.minutesock.wordgame.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minutesock.wordgame.R
import com.minutesock.wordgame.ui.theme.WordGameTheme
import com.minutesock.wordgame.uiutils.UiText
import com.minutesock.wordgame.uiutils.shareExternal

@Composable
fun DailyWordScreenStats(
    state: DailyWordState,
    onStatsEvent: (DailyWordEventStats) -> Unit,
    hasBackgroundScreen: Boolean
) {
    val alpha by remember(hasBackgroundScreen) {
        mutableStateOf(if (hasBackgroundScreen) 0.55f else 1.0f)
    }

    var shareEnabled by remember(state.gameState) { mutableStateOf(state.gameState.isGameOver) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            shareEnabled = true
        }
    val title = stringResource(id = R.string.what_in_da_word)

    LaunchedEffect(state.shareText) {
        state.shareText?.let { shareText ->
            launcher.launch("${title}\n$shareText".shareExternal())
        }
    }

    Box(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.background
                    .copy(alpha = alpha)
            )
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        shareEnabled = false
                        onStatsEvent(DailyWordEventStats.OnShareButtonPressed)
                    },
                    enabled = shareEnabled
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "share")
                }
                IconButton(onClick = { onStatsEvent(DailyWordEventStats.OnExitButtonPressed) }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium,
                    text = state.dailyWordStateMessage?.uiText?.asString() ?: "",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth(0.8f)
            )
            Divider(thickness = 2.dp)
        }
    }
}

@Preview
@Composable
fun DailyWordScreenStatsPreview() {
    WordGameTheme {
        DailyWordScreenStats(
            state = DailyWordState(
                gameState = DailyWordGameState.Success,
                screenState = DailyWordScreenState.Stats,
                dailyWordStateMessage = DailyWordStateMessage(
                    uiText = UiText.DynamicString(
                        value = "Wow great job you solved it wow great job wow!!!!!!!!!"
                    )
                )
            ),
            onStatsEvent = {},
            hasBackgroundScreen = false
        )
    }
}