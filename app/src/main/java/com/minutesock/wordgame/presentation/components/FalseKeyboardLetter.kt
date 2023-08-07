package com.minutesock.wordgame.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minutesock.wordgame.presentation.DailyWordEvent
import com.minutesock.wordgame.presentation.DailyWordState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FalseKeyboardLetter(
    state: DailyWordState,
    onEvent: (DailyWordEvent) -> Unit,
    displayText: String
) {
    TextButton(
        modifier = Modifier
            .size(38.dp, 64.dp)
            .padding(2.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
        onClick = {
            when (displayText) {
                "enter" -> onEvent(DailyWordEvent.OnEnterPress)
                "remove" -> onEvent(DailyWordEvent.OnDeletePress)
                else -> onEvent(DailyWordEvent.OnCharacterPress(displayText.first()))
            }
        }
    ) {
        Text(
            text = displayText,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}