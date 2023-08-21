package com.minutesock.wordgame.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minutesock.wordgame.domain.GuessWordState
import com.minutesock.wordgame.domain.UserGuessLetter
import com.minutesock.wordgame.domain.UserGuessWord
import com.minutesock.wordgame.presentation.DailyWordEventGame
import com.minutesock.wordgame.presentation.GuessWordError
import com.minutesock.wordgame.uiutils.ShakeConfig
import com.minutesock.wordgame.uiutils.rememberShakeController
import com.minutesock.wordgame.uiutils.shake
import kotlinx.collections.immutable.ImmutableList

@Composable
fun WordRow(
    userGuessWord: UserGuessWord,
    userGuessLetters: ImmutableList<UserGuessLetter>,
    onEvent: (DailyWordEventGame) -> Unit
) {
    val shakeController = rememberShakeController()
    LaunchedEffect(userGuessWord.errorState) {
        if (userGuessWord.errorState != GuessWordError.None) {
            shakeController.shake(ShakeConfig.no())
        }
    }

    LaunchedEffect(userGuessWord.state) {
        when (userGuessWord.state) {
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
        userGuessLetters.forEachIndexed { index: Int, userGuessLetter: UserGuessLetter ->
            LetterBox(
                letter = userGuessLetter,
                guessWordState = userGuessWord.state,
                onEvent = onEvent,
                flipAnimDelay = index * 200,
                isFinalLetterInRow = index + 1 == userGuessLetters.size
            )
        }
    }
}