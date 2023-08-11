package com.minutesock.wordgame.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.minutesock.wordgame.presentation.DailyWordEvent

@Composable
fun FalseKeyboardLetter(
    onEvent: (DailyWordEvent) -> Unit,
    displayText: String
) {
    val isLetter by remember { mutableStateOf(displayText.length == 1) }
    val sizeX by remember {
        mutableStateOf(if (isLetter) 38.dp else 50.dp)
    }
    TextButton(
        modifier = Modifier
            .size(sizeX, 55.dp)
            .padding(2.dp),

        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
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
                tint = Color.White
            )

            "remove" -> Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "remove",
                tint = Color.White
            )

            else -> Text(
                textAlign = TextAlign.Center,
                text = displayText,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}