package com.minutesock.core.data

import com.minutesock.core.domain.GuessLetter
import com.minutesock.core.domain.GuessWordState
import com.minutesock.core.presentation.GuessWordError

data class GuessWordStorage(
    val letters: List<GuessLetter>,
    val state: GuessWordState = GuessWordState.Unused,
    val errorState: GuessWordError = GuessWordError.None,
    val completeTime: String? = null,
)
