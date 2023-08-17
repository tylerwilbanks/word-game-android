package com.minutesock.wordgame.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minutesock.wordgame.domain.GuessLetter
import com.minutesock.wordgame.domain.GuessWordState
import com.minutesock.wordgame.presentation.DailyWordEventGame

@Composable
fun LetterBox(
    letter: GuessLetter,
    guessWordState: GuessWordState,
    onEvent: (DailyWordEventGame) -> Unit,
    flipAnimDelay: Int,
    isFinalLetterInRow: Boolean
) {

    val animateColor by animateColorAsState(
        targetValue = if (letter.answered) letter.displayColor(MaterialTheme.colorScheme.background) else MaterialTheme.colorScheme.background,
        animationSpec = tween(
            durationMillis = 1250 / 2 + flipAnimDelay,
            delayMillis = 750 + flipAnimDelay
        )
    )

    var flipRotation by remember { mutableStateOf(0f) }

    LaunchedEffect(guessWordState) {
        if (guessWordState != GuessWordState.Complete) {
            return@LaunchedEffect
        }
        animate(
            initialValue = 360f,
            targetValue = 0f,
            animationSpec =
            tween(
                delayMillis = flipAnimDelay,
                durationMillis = 1250,
                easing = LinearEasing
            )
        ) { value: Float, _: Float ->
            flipRotation = value
        }
        if (isFinalLetterInRow) {
            onEvent(DailyWordEventGame.OnAnsweredWordRowAnimationFinishedGame)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.graphicsLayer {
            rotationX = flipRotation
        }
    ) {
        Card(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = animateColor,
                    shape = RoundedCornerShape(5.dp)
                )
                .graphicsLayer {
                    rotationX = flipRotation
                },
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(containerColor = animateColor),
        ) {}
        Text(
            textAlign = TextAlign.Center,
            text = letter.displayCharacter,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 32.sp
        )
    }
}