package com.minutesock.daily.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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

@Composable
fun FalseKeyboardLetter(
    onEvent: (com.minutesock.daily.presentation.DailyWordEventGame) -> Unit,
    userGuessKey: com.minutesock.daily.domain.UserGuessKey,
    isWordRowAnimating: Boolean = false
) {
    val isLetter by remember { mutableStateOf(userGuessKey.keyName.length == 1) }
    val sizeX by remember {
        mutableStateOf(
            if (isLetter) 35.dp else 50.dp
        )
    }

    val backgroundColor by animateColorAsState(
        targetValue = userGuessKey
            .displayColor(MaterialTheme.colorScheme.secondaryContainer),
        animationSpec = tween(3000), label = "letter background color"
    )

    val buttonEnabled by remember(isWordRowAnimating) {
        mutableStateOf(!(userGuessKey.keyName == "enter" && isWordRowAnimating))
    }

    TextButton(
        modifier = Modifier
            .size(sizeX, 55.dp)
            .padding(2.dp),

        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(10),
        onClick = {
            when (userGuessKey.keyName) {
                "enter" -> {
                    onEvent.invoke(com.minutesock.daily.presentation.DailyWordEventGame.OnEnterPress)
                }

                "remove" -> {
                    onEvent.invoke(com.minutesock.daily.presentation.DailyWordEventGame.OnDeletePress)
                }

                else -> {
                    onEvent.invoke(
                        com.minutesock.daily.presentation.DailyWordEventGame.OnCharacterPress(
                            userGuessKey.character
                        )
                    )
                }
            }
        },
        enabled = buttonEnabled
    ) {
        when (userGuessKey.keyName) {
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
                text = userGuessKey.character.toString(),
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                fontSize = 16.sp
            )
        }
    }
}