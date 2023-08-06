package com.minutesock.wordgame.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minutesock.wordgame.domain.WordGuess
import com.minutesock.wordgame.ui.theme.WordGameTheme

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
                WordGuess(1)
            )
        }
    }
}