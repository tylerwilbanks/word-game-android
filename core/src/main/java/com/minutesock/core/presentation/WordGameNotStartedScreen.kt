package com.minutesock.core.presentation

import android.content.res.Configuration
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minutesock.core.R
import com.minutesock.core.domain.WordGameMode
import com.minutesock.core.domain.WordGameNotStartedEvent
import com.minutesock.core.theme.WordGameTheme

@Composable
fun WordGameNotStartedScreen(
    modifier: Modifier = Modifier,
    gameMode: WordGameMode,
    onEvent: (WordGameNotStartedEvent) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val textColor by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.onSurface,
        targetValue = MaterialTheme.colorScheme.primary,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val gameModeIconId by remember(gameMode) {
        mutableStateOf(
            when (gameMode) {
                WordGameMode.Daily -> R.drawable.baseline_today_24
                WordGameMode.Inifinity -> R.drawable.baseline_infinity
            }
        )
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.what_in_da_word),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            color = textColor
        )
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = {
                onEvent(
                    WordGameNotStartedEvent.OnGameBegin(
                        gameMode = gameMode
                    )
                )
            }
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = gameModeIconId),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Play",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = gameModeIconId),
                contentDescription = null
            )
        }
    }

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WordGameNotStartedScreenPreview() {
    WordGameTheme {
        Surface {
            WordGameNotStartedScreen(
                gameMode = WordGameMode.Inifinity,
                onEvent = { }
            )
        }
    }
}