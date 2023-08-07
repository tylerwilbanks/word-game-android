package com.minutesock.wordgame.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minutesock.wordgame.domain.GuessLetter
import com.minutesock.wordgame.presentation.DailyWordState

@Composable
fun LetterCard(state: DailyWordState, letter: GuessLetter) {
    Card(
        modifier = Modifier
            .size(48.dp),
        colors = CardDefaults.cardColors(containerColor = letter.color),
        border = BorderStroke(2.dp, Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = letter.displayCharacter,
                color = Color.White,
                fontSize = 32.sp
            )
        }
    }
}