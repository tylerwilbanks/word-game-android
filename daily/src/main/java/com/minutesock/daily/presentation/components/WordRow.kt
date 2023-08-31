package com.minutesock.daily.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.minutesock.core.uiutils.shake
import com.minutesock.daily.R
import kotlinx.collections.immutable.ImmutableList

@Composable
fun WordRow(
    userGuessWord: com.minutesock.daily.domain.UserGuessWord,
    userGuessLetters: ImmutableList<com.minutesock.daily.domain.UserGuessLetter>,
    message: String?,
    onEvent: (com.minutesock.daily.presentation.DailyWordEventGame) -> Unit
) {
    val shakeController = com.minutesock.core.uiutils.rememberShakeController()
    val defaultMessage = stringResource(id = R.string.what_in_da_word)
    LaunchedEffect(message) {
        if (
            message != defaultMessage &&
            userGuessWord.state == com.minutesock.daily.domain.GuessWordState.Editing &&
            userGuessWord.errorState != com.minutesock.daily.presentation.GuessWordError.None
        ) {
            shakeController.shake(com.minutesock.core.uiutils.ShakeConfig.no())
        }
    }

    LaunchedEffect(userGuessWord.state) {
        when (userGuessWord.state) {
            com.minutesock.daily.domain.GuessWordState.Correct -> shakeController.shake(com.minutesock.core.uiutils.ShakeConfig.yes())
            com.minutesock.daily.domain.GuessWordState.Failure -> shakeController.shake(com.minutesock.core.uiutils.ShakeConfig.no())
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
        userGuessLetters.forEachIndexed { index: Int, userGuessLetter: com.minutesock.daily.domain.UserGuessLetter ->
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