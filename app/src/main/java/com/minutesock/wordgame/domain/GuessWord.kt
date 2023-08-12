package com.minutesock.wordgame.domain

import com.minutesock.wordgame.utils.Resource
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

enum class GuessWordError {
    Unknown,
    NoWordToEdit,
    NoLettersAvailableForInput,
    NoLettersToRemove;

    val message: String
        get() {
            // todo extract these into string resources
            return when (this) {
                Unknown -> "Unknown Error"
                NoWordToEdit -> "There are no words to edit."
                NoLettersAvailableForInput -> "This word is full."
                NoLettersToRemove -> "This word is empty."
            }
        }
}

fun GuessWord.addGuessLetter(guessLetter: GuessLetter): Resource<GuessWord?> {
    val newGuessLetterList = this.letters.toMutableList()
    newGuessLetterList.indexOfFirst { it.availableForInput }.let { index ->
        if (index == -1) {
            val customError = GuessWordError.NoLettersAvailableForInput
            return Resource.Error(
                message = customError.message,
                errorCode = customError.ordinal
            )
        }
        newGuessLetterList[index] = newGuessLetterList[index].copy(
            _character = guessLetter.character,
            state = guessLetter.state
        )
    }
    return Resource.Success(
        data = this.copy(
            letters = newGuessLetterList.toImmutableList(),
            state = this.state
        )
    )
}

fun GuessWord.eraseLetter(): Resource<GuessWord?> {
    val newGuessLetterList = this.letters.toMutableList()
    newGuessLetterList.indexOfLast { it.answered }.let { index ->
        if (index == -1) {
            val customError = GuessWordError.NoLettersToRemove
            return Resource.Error(
                message = customError.message,
                errorCode = customError.ordinal
            )
        }

        newGuessLetterList[index] = newGuessLetterList[index].erase()

    }
    return Resource.Success(
        data = this.copy(
            letters = newGuessLetterList.toImmutableList(),
            state = this.state
        )
    )
}

fun GuessWord.updateState(newState: GuessWordState): GuessWord {
    return this.copy(
        letters = this.letters,
        state = newState
    )
}
