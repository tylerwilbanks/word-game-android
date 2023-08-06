package com.minutesock.wordgame.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minutesock.wordgame.domain.Letter
import com.minutesock.wordgame.domain.WordGuess
import com.minutesock.wordgame.ui.theme.WordGameTheme

@Composable
fun LetterCard(letter: Letter) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .size(64.dp),
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

@Composable
fun WordRow(
    guess: WordGuess
) {
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        LetterCard(guess.letters[0])
        LetterCard(guess.letters[1])
        LetterCard(guess.letters[2])
        LetterCard(guess.letters[3])
        LetterCard(guess.letters[4])
    }
}

@Preview(showBackground = true)
@Composable
fun WordPreview() {
    WordGameTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            WordRow(
                WordGuess()
            )
        }
    }
}