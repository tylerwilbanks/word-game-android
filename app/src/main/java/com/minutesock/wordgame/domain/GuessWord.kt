package com.minutesock.wordgame.domain

class GuessWord {
    private val wordSize = 5

    val letters = List(wordSize) {
        GuessLetter()
    }

    private var _lockedIn = false
    val lockedIn get() = _lockedIn

    fun updateGuess(correctWord: String) {
        _lockedIn = true

        letters.forEachIndexed { index: Int, guessLetter: GuessLetter ->
            guessLetter.state = when {
                !correctWord.contains(guessLetter.character) -> LetterState.Absent
                correctWord[index] == guessLetter.character -> LetterState.Correct
                else -> LetterState.Present
            }
        }
    }

    val displayWord get() = letters.joinToString(separator = "") { it.displayCharacter }.uppercase()
    val word get() = letters.joinToString(separator = "") { it.displayCharacter }.lowercase()

    val getLetterIndexForInput: Int
        get() {
            val index = letters.indexOfFirst { it.availableForInput }
            return if (index == -1) {
                letters.size - 1
            } else {
                index
            }
        }

    val getLetterForInput: GuessLetter? get() = letters.firstOrNull { it.availableForInput }
    val getLetterToErase: GuessLetter? get() = letters.lastOrNull { !it.availableForInput }

    val isIncomplete get() = letters.any { it.availableForInput }
}