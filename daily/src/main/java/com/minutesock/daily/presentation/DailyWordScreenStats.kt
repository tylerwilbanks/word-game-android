package com.minutesock.daily.presentation

import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minutesock.core.theme.WordGameTheme
import com.minutesock.core.uiutils.UiText
import com.minutesock.core.uiutils.shareExternal
import com.minutesock.core.utils.capitalize
import com.minutesock.daily.R
import com.minutesock.daily.presentation.components.WordInfoItem

@OptIn(ExperimentalMaterial3Api::class)
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

    val textResultColor =
        if (state.dailyWordStateMessage?.isError == true) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary

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
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    text = state.dailyWordStateMessage?.uiText?.asString() ?: "",
                    color = textResultColor
                )
            }
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )

            Scaffold(
                topBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .border(
                                    border = BorderStroke(
                                        2.dp,
                                        MaterialTheme.colorScheme.secondary
                                    ),
                                    shape = RoundedCornerShape(corner = CornerSize(10.dp))
                                )
                                .blur(spoilerBlur)
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 25.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineMedium,
                            text = state.correctWord?.capitalize() ?: "",
                            color = textResultColor
                        )
                    }
                },
                bottomBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                revealSpoiler = !revealSpoiler
                            },
                            enabled = spoilerButtonEnabled,
                        ) {
                            Icon(
                                painterResource(id = if (revealSpoiler) com.minutesock.core.R.drawable.baseline_visibility_off_24 else com.minutesock.core.R.drawable.baseline_visibility_24),
                                contentDescription = "Show/Hide"
                            )
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                            Text(
                                text = if (revealSpoiler) {
                                    stringResource(R.string.hide)
                                } else {
                                    stringResource(
                                        R.string.reveal
                                    )
                                }
                            )
                        }
                        Button(
                            onClick = {
                                shareEnabled = false
                                onEvent(DailyWordEventStats.OnShareButtonPressed)
                            },
                            enabled = shareEnabled,
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                            Text(
                                text = stringResource(R.string.share)
                            )
                        }
                    }

                },
                content = { padding ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(spoilerBlur)
                            .padding(
                                bottom = padding.calculateBottomPadding(),
                                top = padding.calculateTopPadding(),
                                start = 20.dp,
                                end = 20.dp
                            )
                    ) {
                        items(state.wordInfos.size) { i ->
                            val wordInfo = state.wordInfos[i]
                            if (i > 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            WordInfoItem(wordInfo = wordInfo, gameState = state.gameState)
                            if (i < state.wordInfos.size - 1) {
                                Divider()
                            }
                        }
                    }
                }
            )
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