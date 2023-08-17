package com.minutesock.wordgame.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minutesock.wordgame.domain.GuessLetter
import com.minutesock.wordgame.domain.GuessWord
import com.minutesock.wordgame.domain.GuessWordState
import com.minutesock.wordgame.presentation.DailyWordEventGame
import com.minutesock.wordgame.presentation.GuessWordError
import com.minutesock.wordgame.uiutils.ShakeConfig
import com.minutesock.wordgame.uiutils.rememberShakeController
import com.minutesock.wordgame.uiutils.shake
import kotlinx.collections.immutable.ImmutableList

@Composable
fun WordRow(
    guessWord: GuessWord,
    guessLetters: ImmutableList<GuessLetter>,
    onEvent: (DailyWordEventGame) -> Unit
) {
    val shakeController = rememberShakeController()
    LaunchedEffect(guessWord.errorState) {
        if (guessWord.errorState != GuessWordError.None) {
            shakeController.shake(ShakeConfig.no())
        }
    }

    LaunchedEffect(guessWord.state) {
        when (guessWord.state) {
            GuessWordState.Correct -> shakeController.shake(ShakeConfig.yes())
            GuessWordState.Failure -> shakeController.shake(ShakeConfig.no())
            else -> {}
        }
    }

    Row(
        modifier = Modifier
            .padding(5.dp)
            .shake(
                shakeController = shakeController,
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        guessLetters.forEachIndexed { index: Int, guessLetter: GuessLetter ->
            LetterBox(
                letter = guessLetter,
                guessWordState = guessWord.state,
                onEvent = onEvent,
                flipAnimDelay = index * 200,
                isFinalLetterInRow = index + 1 == guessLetters.size
            )
        }
    }
}