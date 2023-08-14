package com.minutesock.wordgame.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minutesock.wordgame.domain.GuessKey
import com.minutesock.wordgame.presentation.DailyWordEvent
import kotlinx.collections.immutable.ImmutableList

@Composable
fun FalseKeyboardLetter(
    onEvent: (DailyWordEvent) -> Unit,
    displayText: String,
    guessKeys: ImmutableList<GuessKey>,
) {
    val isLetter by remember { mutableStateOf(displayText.length == 1) }
    val sizeX by remember {
        mutableStateOf(if (isLetter) 35.dp else 50.dp)
    }

    val backgroundColor = if (isLetter) {
        guessKeys.firstOrNull { it.character == displayText.firstOrNull() }
            ?.displayColor(MaterialTheme.colorScheme.secondaryContainer)
            ?: MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }

    TextButton(
        modifier = Modifier
            .size(sizeX, 55.dp)
            .padding(2.dp),

        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(10),
        onClick = {
            when (displayText) {
                "enter" -> {
                    onEvent.invoke(DailyWordEvent.OnEnterPress)
                }

                "remove" -> {
                    onEvent.invoke(DailyWordEvent.OnDeletePress)
                }

                else -> {
                    onEvent.invoke(DailyWordEvent.OnCharacterPress(displayText.first()))
                }
            }
        }
    ) {
        when (displayText) {
            "enter" -> Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "enter",
                tint = MaterialTheme.colorScheme.primary
            )

            "remove" -> Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "remove",
                tint = if (isSystemInDarkTheme()) Color.White else Color.Black
            )

            else -> Text(
                textAlign = TextAlign.Center,
                text = displayText,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                fontSize = 16.sp
            )
        }
    }
}