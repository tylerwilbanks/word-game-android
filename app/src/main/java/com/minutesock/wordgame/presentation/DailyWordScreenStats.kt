package com.minutesock.wordgame.presentation

import android.content.res.Configuration
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minutesock.wordgame.R
import com.minutesock.wordgame.ui.theme.WordGameTheme
import com.minutesock.wordgame.uiutils.UiText
import com.minutesock.wordgame.uiutils.shareExternal
import java.util.Locale

@Composable
fun DailyWordScreenStats(
    state: DailyWordState,
    onEvent: (DailyWordEventStats) -> Unit,
    hasBackgroundScreen: Boolean
) {
    val alpha by remember(hasBackgroundScreen) {
        mutableStateOf(if (hasBackgroundScreen) 0.55f else 1.0f)
    }

    var shareEnabled by remember(state.gameState) { mutableStateOf(state.gameState.isGameOver) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            shareEnabled = true
            onEvent(DailyWordEventStats.OnShareChooserPresented)
        }

    val title = stringResource(id = R.string.what_in_da_word)

    LaunchedEffect(state.shareText) {
        state.shareText?.let { shareText ->
            launcher.launch("${title}\n$shareText".shareExternal())
        }
    }

    var revealSpoiler by remember(state.gameState) {
        mutableStateOf(state.gameState == DailyWordGameState.Success)
    }

    val spoilerBlur by remember(revealSpoiler) {
        mutableStateOf(if (revealSpoiler) 0.dp else 10.dp)
    }

    val spoilerButtonEnabled by remember(state.gameState) {
        mutableStateOf(state.gameState.isGameOver)
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
                        onEvent(DailyWordEventStats.OnShareButtonPressed)
                    },
                    enabled = shareEnabled
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "share")
                }
                IconButton(onClick = { onEvent(DailyWordEventStats.OnExitButtonPressed) }) {
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
                    color = if (state.dailyWordStateMessage?.isError == true) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            Divider(thickness = 2.dp, modifier = Modifier.padding(horizontal = 20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .blur(spoilerBlur),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium,
                    text = state.correctWord?.capitalize(Locale.ROOT) ?: "",
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    modifier = Modifier
                        .blur(spoilerBlur),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    text = state.definitionMessage ?: "",
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(
                    onClick = {
                        revealSpoiler = !revealSpoiler
                    },
                    enabled = spoilerButtonEnabled
                ) {
                    Text(text = if (revealSpoiler) "Hide" else "Reveal")
                }
            }

        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DailyWordScreenStatsPreview() {
    WordGameTheme {
        Surface {
            DailyWordScreenStats(
                state = DailyWordState(
                    gameState = DailyWordGameState.Success,
                    screenState = DailyWordScreenState.Stats,
                    dailyWordStateMessage = DailyWordStateMessage(
                        uiText = UiText.DynamicString(
                            value = "Wow great job you solved it wow great job wow!!!!!!!!!"
                        )
                    ),
                    correctWord = "Jumby"
                ),
                onEvent = {},
                hasBackgroundScreen = false
            )
        }
    }
}