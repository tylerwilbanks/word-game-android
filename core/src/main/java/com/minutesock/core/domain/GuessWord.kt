package com.minutesock.core.domain

import com.minutesock.core.presentation.GuessWordError
import com.minutesock.core.utils.Option
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class GuessWord(
    val letters: ImmutableList<GuessLetter>,
    val state: GuessWordState = GuessWordState.Unused,
    val errorState: GuessWordError = GuessWordError.None,
    val completeTime: Instant? = null,
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


fun GuessWord.addGuessLetter(guessLetter: GuessLetter): Option<GuessWord?> {
    val newGuessLetterList = this.letters.toMutableList()
    newGuessLetterList.indexOfFirst { it.availableForInput }.let { index ->
        if (index == -1) {
            val customError = GuessWordError.NoLettersAvailableForInput
            return Option.Error(
                uiText = customError.message,
                errorCode = customError.ordinal
            )
        }
        newGuessLetterList[index] = newGuessLetterList[index].copy(
            _character = guessLetter.character,
            state = guessLetter.state
        )
    }
    return Option.Success(
        data = this.copy(
            letters = newGuessLetterList.toImmutableList(),
            state = this.state
        )
    )
}

fun GuessWord.eraseLetter(): Option<GuessWord?> {
    val newGuessLetterList = this.letters.toMutableList()
    newGuessLetterList.indexOfLast { it.answered }.let { index ->
        if (index == -1) {
            val customError = GuessWordError.NoLettersToRemove
            return Option.Error(
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

fun GuessWord.updateState(newState: GuessWordState): GuessWord {
    return this.copy(
        letters = this.letters,
        state = newState
    )
}

fun GuessWord.lockInGuess(correctWord: String, isFinalGuess: Boolean): GuessWord {
    val newGuessLetters = mutableListOf<GuessLetter>()
    val correctChars: List<Char> = correctWord.map { it.lowercaseChar() }

    this.letters.forEachIndexed { index: Int, guessLetter: GuessLetter ->
        val newState = when {
            guessLetter.character == correctChars[index] -> LetterState.Correct
            correctChars.contains(guessLetter.character) -> LetterState.Present
            else -> LetterState.Absent
        }
        newGuessLetters.add(
            GuessLetter(
                _character = guessLetter.character,
                state = newState
            )
        )
    }

    //clean up any extra present letters
    this.letters.forEachIndexed { index, userGuessLetter ->
        val correctDuplicateLetterCount = correctChars.count { it == userGuessLetter.character }
        val currentPresentAndCorrectLetterCount = newGuessLetters.count {
            it.character == this.letters[index].character && it.state == LetterState.Correct ||
                    it.character == this.letters[index].character && it.state == LetterState.Present
        }

        if (newGuessLetters[index].state == LetterState.Present && currentPresentAndCorrectLetterCount > correctDuplicateLetterCount) {
            newGuessLetters[index] = newGuessLetters[index].copy(
                state = LetterState.Absent
            )
        }

    }

    return this.copy(
        letters = newGuessLetters.toImmutableList(),
        state = updateStateAfterGuess(correctWord, isFinalGuess),
        completeTime = Clock.System.now()
    )
}

private fun GuessWord.updateStateAfterGuess(
    correctWord: String,
    isFinalGuess: Boolean
): GuessWordState {
    return when {
        this.word.lowercase() == correctWord.lowercase() -> GuessWordState.Correct
        !isFinalGuess && this.word.lowercase() != correctWord.lowercase() -> GuessWordState.Complete
        isFinalGuess && this.word.lowercase() != correctWord.lowercase() -> GuessWordState.Failure
        else -> GuessWordState.Complete
    }
}
