package com.minutesock.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.minutesock.core.R
import com.minutesock.core.domain.GuessLetter
import com.minutesock.core.domain.GuessWord
import com.minutesock.core.domain.GuessWordState
import com.minutesock.core.presentation.GuessWordError
import com.minutesock.core.presentation.WordEventGame
import com.minutesock.core.uiutils.ShakeConfig
import com.minutesock.core.uiutils.rememberShakeController
import com.minutesock.core.uiutils.shake
import kotlinx.collections.immutable.ImmutableList

@Composable
fun WordRow(
    guessWord: GuessWord,
    guessLetters: ImmutableList<GuessLetter>,
    message: String?,
    wordRowAnimating: Boolean,
    onEvent: (WordEventGame) -> Unit
) {
    val shakeController = rememberShakeController()
    val defaultMessage = stringResource(id = R.string.what_in_da_word)
    LaunchedEffect(message) {
        if (wordRowAnimating) {
            return@LaunchedEffect
        }
        if (
            message != defaultMessage &&
            guessWord.state == GuessWordState.Editing &&
            guessWord.errorState != GuessWordError.None
        ) {
            shakeController.shake(ShakeConfig.no())
        }

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