package com.minutesock.daily.domain

import com.minutesock.daily.presentation.GuessWordError
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class UserGuessWord(
    val letters: ImmutableList<UserGuessLetter>,
    val state: GuessWordState = GuessWordState.Unused,
    val errorState: GuessWordError = GuessWordError.None
) {
    val word: String get() = letters.joinToString("") { it.displayCharacter }.lowercase()
    val displayWord: String get() = letters.joinToString("") { it.displayCharacter }.uppercase()

    val isIncomplete: Boolean get() = letters.any { it.availableForInput }
}

enum class GuessWordState {
    Unused,
    Editing,
    Complete,
    Correct,
    Failure
}


fun UserGuessWord.addGuessLetter(userGuessLetter: UserGuessLetter): com.minutesock.core.utils.Option<UserGuessWord?> {
    val newGuessLetterList = this.letters.toMutableList()
    newGuessLetterList.indexOfFirst { it.availableForInput }.let { index ->
        if (index == -1) {
            val customError = GuessWordError.NoLettersAvailableForInput
            return com.minutesock.core.utils.Option.Error(
                uiText = customError.message,
                errorCode = customError.ordinal
            )
        }
        newGuessLetterList[index] = newGuessLetterList[index].copy(
            _character = userGuessLetter.character,
            state = userGuessLetter.state
        )
    }
    return com.minutesock.core.utils.Option.Success(
        data = this.copy(
            letters = newGuessLetterList.toImmutableList(),
            state = this.state
        )
    )
}

fun UserGuessWord.eraseLetter(): com.minutesock.core.utils.Option<UserGuessWord?> {
    val newGuessLetterList = this.letters.toMutableList()
    newGuessLetterList.indexOfLast { it.answered }.let { index ->
        if (index == -1) {
            val customError = GuessWordError.NoLettersToRemove
            return com.minutesock.core.utils.Option.Error(
                uiText = customError.message,
                errorCode = customError.ordinal
            )
        }

        newGuessLetterList[index] = newGuessLetterList[index].erase()

    }
    return com.minutesock.core.utils.Option.Success(
        data = this.copy(
            letters = newGuessLetterList.toImmutableList(),
            state = this.state
        )
    )
}

fun UserGuessWord.updateState(newState: GuessWordState): UserGuessWord {
    return this.copy(
        letters = this.letters,
        state = newState
    )
}

fun UserGuessWord.lockInGuess(correctWord: String): UserGuessWord {
    val newUserGuessLetters = mutableListOf<UserGuessLetter>()
    val correctChars: List<Char> = correctWord.map { it.lowercaseChar() }

    this.letters.forEachIndexed { index: Int, userGuessLetter: UserGuessLetter ->
        val newState = when {
            userGuessLetter.character == correctChars[index] -> UserLetterState.Correct
            correctChars.contains(userGuessLetter.character) -> UserLetterState.Present
            else -> UserLetterState.Absent
        }
        newUserGuessLetters.add(
            UserGuessLetter(
                _character = userGuessLetter.character,
                state = newState
            )
        )
    }

    //clean up any extra present letters
    this.letters.forEachIndexed { index, userGuessLetter ->
        val correctDuplicateLetterCount = correctChars.count { it == userGuessLetter.character }
        val currentPresentAndCorrectLetterCount = newUserGuessLetters.count {
            it.character == this.letters[index].character && it.state == UserLetterState.Correct ||
                    it.character == this.letters[index].character && it.state == UserLetterState.Present
        }

        if (newUserGuessLetters[index].state == UserLetterState.Present && currentPresentAndCorrectLetterCount > correctDuplicateLetterCount) {
            newUserGuessLetters[index] = newUserGuessLetters[index].copy(
                state = UserLetterState.Absent
            )
        }

    }

    return this.copy(
        letters = newUserGuessLetters.toImmutableList(),
        state = GuessWordState.Complete
    )
}
