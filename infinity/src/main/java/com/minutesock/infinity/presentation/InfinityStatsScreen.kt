package com.minutesock.infinity.presentation

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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import com.minutesock.core.R
import com.minutesock.core.domain.WordGameState
import com.minutesock.core.domain.DailyWordState
import com.minutesock.core.presentation.WordEventStats
import com.minutesock.core.presentation.components.WordInfoItem
import com.minutesock.core.utils.capitalize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfinityStatsScreen(
    state: DailyWordState,
    onEvent: (WordEventStats) -> Unit,
    hasBackgroundScreen: Boolean
) {

    val alpha by remember(hasBackgroundScreen) {
        mutableStateOf(if (hasBackgroundScreen) 0.55f else 1.0f)
    }

    var revealSpoiler by remember(state.gameState) {
        mutableStateOf(state.gameState == WordGameState.Success)
    }

    val spoilerBlur by remember(revealSpoiler) {
        mutableStateOf(if (revealSpoiler) 0.dp else 10.dp)
    }

    val spoilerButtonEnabled by remember(state.gameState) {
        mutableStateOf(state.gameState.isGameOver)
    }

    val textResultColor =
        if (state.dailyWordStateMessage?.isError == true || state.gameState == WordGameState.Failure) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary

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
                IconButton(onClick = { onEvent(WordEventStats.OnExitButtonPressed) }) {
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
                                painterResource(id = if (revealSpoiler) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24),
                                contentDescription = "Show/Hide"
                            )
                            Spacer(modifier = Modifier.size(5.dp))
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
                                onEvent(WordEventStats.OnInfinityNextSessionPressed)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            enabled = state.gameState.isGameOver
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next"
                            )
                            Spacer(modifier = Modifier.size(2.dp))
                            Text(
                                text = "Next"
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
                            WordInfoItem(
                                wordInfo = wordInfo,
                                wordColor = if (state.gameState == WordGameState.Failure) {
                                    MaterialTheme.colorScheme.error
                                }
                                else {
                                    MaterialTheme.colorScheme.primary
                                }
                            )
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