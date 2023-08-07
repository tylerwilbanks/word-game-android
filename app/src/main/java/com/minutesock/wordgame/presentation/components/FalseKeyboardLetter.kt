package com.minutesock.wordgame.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    Card(
        modifier = Modifier
            .size(38.dp, 60.dp)
            .padding(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = {
            when (displayText) {
                "enter" -> onEvent(DailyWordEvent.OnEnterPress)
                "remove" -> onEvent(DailyWordEvent.OnDeletePress)
                else -> onEvent(DailyWordEvent.OnCharacterPress(displayText.first()))
            }
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = displayText,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}