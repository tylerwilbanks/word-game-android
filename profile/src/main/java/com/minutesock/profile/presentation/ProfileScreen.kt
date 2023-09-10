package com.minutesock.profile.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minutesock.core.domain.DailyWordGameState
import com.minutesock.core.theme.WordGameTheme
import com.minutesock.profile.R
import com.minutesock.profile.domain.GuessDistribution
import com.minutesock.profile.domain.GuessDistributionState
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ProfileScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    profileViewModel: ProfileViewModel = viewModel()
) {

    val state by profileViewModel.guessDistributionState.collectAsStateWithLifecycle()
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                profileViewModel.updateGuessDistribution()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    GuessDistributionPanel(guessDistributionState = state)
}

@Composable
fun GuessDistributionStat(
    correctAttemptCount: Int,
    rowColor: Color,
    textColor: Color,
    attemptText: String,
    maxCorrectAttemptCount: Int,
    animDelay: Int,
) {

    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val currentPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            correctAttemptCount.toFloat() / maxCorrectAttemptCount.coerceAtLeast(1)
        } else {
            0f
        },
        animationSpec = tween(
            1000,
            animDelay
        ), label = ""
    )
    LaunchedEffect(true) {
        animationPlayed = true
    }

    Box(
        modifier = Modifier
            .width(300.dp)
            .height(30.dp)
            .padding(5.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(currentPercent.value.coerceAtLeast(0.15f))
                .fillMaxHeight()
                .background(rowColor)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = attemptText,
                color = textColor,

                )
            Text(
                text = (currentPercent.value * maxCorrectAttemptCount).toInt().toString(),
                color = textColor
            )
        }
    }
}

@Composable
fun GuessDistributionPanel(
    guessDistributionState: GuessDistributionState
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(5.dp)
            ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.guess_distribution),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Divider()
            Spacer(modifier = Modifier.height(10.dp))
            guessDistributionState.guessDistributions.forEachIndexed { index, guessDistribution ->
                GuessDistributionStat(
                    correctAttemptCount = guessDistribution.correctAttemptCount,
                    rowColor = lerp(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.tertiaryContainer,
                        (index + 1) / guessDistributionState.guessDistributions.size.toFloat()
                    ),
                    textColor = lerp(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.tertiary,
                        (index + 1) / guessDistributionState.guessDistributions.size.toFloat()
                    ),
                    maxCorrectAttemptCount = guessDistributionState.maxCorrectAttemptCount,
                    animDelay = (guessDistributionState.guessDistributions.size * 100) - (index * 100),
                    attemptText = guessDistribution.correctAttempt.toString()
                )
            }

            GuessDistributionStat(
                correctAttemptCount = guessDistributionState.failedGameSessionsCount,
                rowColor = MaterialTheme.colorScheme.errorContainer,
                textColor = MaterialTheme.colorScheme.error,
                maxCorrectAttemptCount = guessDistributionState.maxCorrectAttemptCount,
                animDelay = 0,
                attemptText = "X"
            )
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    val dummyState = createDummyGuessDistributionState()

    WordGameTheme {
        GuessDistributionPanel(
            guessDistributionState = dummyState
        )
    }
}

private fun createDummyGuessDistributionState(): GuessDistributionState {
    val guessDistributions = mutableListOf<GuessDistribution>()
    val maxGuessAttemptsSize = 6
    for (i in 1..maxGuessAttemptsSize) {
        guessDistributions.add(
            GuessDistribution(
                correctAttempt = i,
                correctAttemptCount = i * 2,
                gameState = DailyWordGameState.Success,
                maxGuessAttempts = maxGuessAttemptsSize,
            )
        )
    }
    return GuessDistributionState(
        loading = false,
        guessDistributions = guessDistributions.toImmutableList(),
        maxCorrectAttemptCount = 12,
        failedGameSessionsCount = 6
    )
}