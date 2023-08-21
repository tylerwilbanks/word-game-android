package com.minutesock.wordgame.domain

import com.minutesock.wordgame.presentation.GuessWordError
import com.minutesock.wordgame.utils.Option
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


fun UserGuessWord.addGuessLetter(userGuessLetter: UserGuessLetter): Option<UserGuessWord?> {
    val newGuessLetterList = this.letters.toMutableList()
    newGuessLetterList.indexOfFirst { it.availableForInput }.let { index ->
        if (index == -1) {
            val customError = GuessWordError.NoLettersAvailableForInput
            return Option.UiError(
                uiText = customError.message,
                errorCode = customError.ordinal
            )
        }
        newGuessLetterList[index] = newGuessLetterList[index].copy(
            _character = userGuessLetter.character,
            state = userGuessLetter.state
        )
    }
    return Option.Success(
        data = this.copy(
            letters = newGuessLetterList.toImmutableList(),
            state = this.state
        )
    )
}

fun UserGuessWord.eraseLetter(): Option<UserGuessWord?> {
    val newGuessLetterList = this.letters.toMutableList()
    newGuessLetterList.indexOfLast { it.answered }.let { index ->
        if (index == -1) {
            val customError = GuessWordError.NoLettersToRemove
            return Option.UiError(
                uiText = customError.message,
                errorCode = customError.ordinal
            )
        }

        newGuessLetterList[index] = newGuessLetterList[index].erase()

    }
    return Option.Success(
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
    val processedLetters = hashMapOf<Char, Int>()

    this.letters.forEachIndexed { index: Int, userGuessLetter: UserGuessLetter ->
        val duplicateLetterCount = correctChars.count { it == userGuessLetter.character }
        if (processedLetters.get(userGuessLetter.character) == null) {
            processedLetters[userGuessLetter.character] = 0
        }
        val newState = when {
            userGuessLetter.character == correctChars[index] -> UserLetterState.Correct
            correctChars.contains(userGuessLetter.character) && processedLetters[userGuessLetter.character]!! < duplicateLetterCount -> UserLetterState.Present
            !correctChars.contains(userGuessLetter.character) -> UserLetterState.Absent
            else -> UserLetterState.Absent
        }
        processedLetters[userGuessLetter.character]?.let {
            processedLetters[userGuessLetter.character] = it + 1
        }
        newUserGuessLetters.add(
            UserGuessLetter(
                _character = userGuessLetter.character,
                state = newState
            )
        )
    }

    return this.copy(
        letters = newUserGuessLetters.toImmutableList(),
        state = GuessWordState.Complete
    )
}
