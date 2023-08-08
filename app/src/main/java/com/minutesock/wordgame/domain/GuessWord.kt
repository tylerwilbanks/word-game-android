package com.minutesock.wordgame.domain

class GuessWord {
    private val wordSize = 5

    val letters = List(wordSize) {
        GuessLetter()
    }

    val lockedIn = false

    val word get() = letters.joinToString { it.displayCharacter }

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