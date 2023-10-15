package com.minutesock.core.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.minutesock.core.domain.GuessLetter
import com.minutesock.core.domain.GuessWord
import com.minutesock.core.domain.GuessWordState
import com.minutesock.core.domain.LetterState
import com.minutesock.core.presentation.components.WordRow
import com.minutesock.core.theme.WordGameTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun HowToPlayScreenContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    wordLength: Int = 5,
    maxAttempts: Int = 6,
    exampleWords: ImmutableList<GuessWord>
) {
    val scroll = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(10.dp)
            .scrollable(
                state = scroll,
                orientation = Orientation.Vertical
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "close",
                )
            }
        }

        Text(
            text = "How To Play",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Text(
            text = "Guess the word in $maxAttempts tries.",
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "\u2022 Each guess must be a valid $wordLength letter word.",
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "\u2022 The color of the tiles will change to show how close your guess was to the word.",
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Examples:",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(5.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WordRow(
                guessWord = exampleWords[0],
                guessLetters = exampleWords[0].letters,
                message = "",
                wordRowAnimating = false,
                onEvent = {}
            )

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("W ")
                    }
                    append("is in the word and in the correct spot.")

                },
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            WordRow(
                guessWord = exampleWords[1],
                guessLetters = exampleWords[1].letters,
                message = "",
                wordRowAnimating = false,
                onEvent = {}
            )

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("U ")
                    }
                    append("is in the word, but not in the right spot.")

                },
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            WordRow(
                guessWord = exampleWords[2],
                guessLetters = exampleWords[2].letters,
                message = "",
                wordRowAnimating = false,
                onEvent = {}
            )

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("None ")
                    }
                    append("of these letters are found in the word.")

                },
                fontSize = 16.sp
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HowToPlayScreenPreview() {
    WordGameTheme {
        Surface {
            HowToPlayScreen(
                navController = NavController(LocalContext.current)
            )
        }
    }
}

@Composable
fun HowToPlayScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val word1 = GuessWord(
        state = GuessWordState.Complete,
        letters = listOf(
            GuessLetter('W', LetterState.Correct),
            GuessLetter('H'),
            GuessLetter('I'),
            GuessLetter('L'),
            GuessLetter('E')
        ).toImmutableList()
    )

    val word2 = GuessWord(
        state = GuessWordState.Complete,
        letters = listOf(
            GuessLetter('J'),
            GuessLetter('U', LetterState.Present),
            GuessLetter('M'),
            GuessLetter('B'),
            GuessLetter('O')
        ).toImmutableList()
    )

    val word3 = GuessWord(
        state = GuessWordState.Complete,
        letters = listOf(
            GuessLetter('S'),
            GuessLetter('M'),
            GuessLetter('E', LetterState.Absent),
            GuessLetter('A'),
            GuessLetter('R')
        ).toImmutableList()
    )

    HowToPlayScreenContent(
        modifier = modifier,
        navController = navController,
        exampleWords = persistentListOf(word1, word2, word3)
    )
}