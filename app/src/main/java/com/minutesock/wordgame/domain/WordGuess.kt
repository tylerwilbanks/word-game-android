package com.minutesock.wordgame.domain

class WordGuess {
    val letters = List(5) {
        Letter()
    }
    val word get() = letters.joinToString { it.displayCharacter }
    val getNextAvailableLetterIndexForInput get() = letters.indexOfFirst { it.availableForInput }
    val isIncomplete get() = letters.any { it.availableForInput }
}