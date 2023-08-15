package com.minutesock.wordgame.domain

import com.minutesock.wordgame.presentation.GuessWordError
import com.minutesock.wordgame.utils.Option
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class GuessWord(
    val letters: ImmutableList<GuessLetter>,
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


fun GuessWord.addGuessLetter(guessLetter: GuessLetter): Option<GuessWord?> {
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

fun GuessWord.updateState(newState: GuessWordState): GuessWord {
    return this.copy(
        letters = this.letters,
        state = newState
    )
}

fun GuessWord.lockInGuess(correctWord: String): GuessWord {
    val newGuessLetters = mutableListOf<GuessLetter>()
    val correctChars: List<Char> = correctWord.map { it.lowercaseChar() }
    val processedLetters = hashMapOf<Char, Int>()

    this.letters.forEachIndexed { index: Int, guessLetter: GuessLetter ->
        val duplicateLetterCount = correctChars.count { it == guessLetter.character }
        if (processedLetters.get(guessLetter.character) == null) {
            processedLetters[guessLetter.character] = 0
        }
        val newState = when {
            guessLetter.character == correctChars[index] -> LetterState.Correct
            correctChars.contains(guessLetter.character) && processedLetters[guessLetter.character]!! <= duplicateLetterCount -> LetterState.Present
            !correctChars.contains(guessLetter.character) -> LetterState.Absent
            else -> LetterState.Absent
        }
        processedLetters[guessLetter.character]?.let {
            processedLetters[guessLetter.character] = it + 1
        }
        newGuessLetters.add(
            GuessLetter(
                _character = guessLetter.character,
                state = newState
            )
        )
    }

    return this.copy(
        letters = newGuessLetters.toImmutableList(),
        state = GuessWordState.Complete
    )
}
