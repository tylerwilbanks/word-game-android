package com.minutesock.wordgame.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minutesock.wordgame.domain.GuessLetter

@Composable
fun LetterBox(letter: GuessLetter) {
    val rotated by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(500)
    )

    val animateColor by animateColorAsState(
        targetValue = if (rotated) letter.color else MaterialTheme.colorScheme.background,
        animationSpec = tween(500)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(5.dp)
            )
            .background(animateColor, shape = RoundedCornerShape(5.dp))
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            },
    ) {
        Text(
            modifier = Modifier
                .graphicsLayer {
                    rotationY = rotation
                },
            text = letter.displayCharacter,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 32.sp
        )
    }
}