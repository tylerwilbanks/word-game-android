package com.minutesock.wordgame.domain

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class GuessWord(
    val letters: ImmutableList<GuessLetter>,
    val state: GuessWordState = GuessWordState.Unused
)

enum class GuessWordState {
    Unused,
    Editing,
    Complete
}

fun GuessWord.addGuessLetter(guessLetter: GuessLetter): GuessWord? {
    val newGuessLetterList = this.letters.toMutableList()
    newGuessLetterList.indexOfFirst { it.availableForInput }.let { index ->
        if (index == -1) {
            // todo error handling here
            return null
        }
        newGuessLetterList[index] = newGuessLetterList[index].copy(
            _character = guessLetter.character,
            state = guessLetter.state
        )
    }
    return this.copy(
        letters = newGuessLetterList.toImmutableList(),
        state = this.state
    )
}

fun GuessWord.updateState(newState: GuessWordState): GuessWord {
    return this.copy(
        letters = this.letters,
        state = newState
    )
}
