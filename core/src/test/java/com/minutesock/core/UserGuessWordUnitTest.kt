package com.minutesock.core

import com.minutesock.core.domain.GuessLetter
import com.minutesock.core.domain.GuessWord
import com.minutesock.core.domain.GuessWordState
import com.minutesock.core.domain.LetterState
import com.minutesock.core.domain.lockInGuess
import junit.framework.TestCase.assertEquals
import kotlinx.collections.immutable.toImmutableList
import org.junit.Test

class UserGuessWordUnitTest {

    @Test
    fun duplicateCharacters_singleIncorrectPositionBeforeCorrectPosition() {
        val correctWord = "cedar"
        val guessWord = "radar"
        val userGuessWord = GuessWord(
            letters = List(5) {
                GuessLetter(guessWord[it])
            }.toImmutableList(),
            state = GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord)
        assertEquals(
            LetterState.Absent,
            updatedGuessWord.letters.first().state
        )
    }

    @Test
    fun duplicateCharacters_multipleIncorrectPositionsBeforeCorrectPosition() {
        val correctWord = "razor"
        val guessWord = "error"
        val userGuessWord = GuessWord(
            letters = List(5) {
                GuessLetter(guessWord[it])
            }.toImmutableList(),
            state = GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord)
        assertEquals(
            LetterState.Absent,
            updatedGuessWord.letters[1].state
        )
        assertEquals(
            LetterState.Present,
            updatedGuessWord.letters[2].state
        )
        assertEquals(
            LetterState.Correct,
            updatedGuessWord.letters[4].state
        )
    }

    @Test
    fun duplicateCharacters_singleIncorrectPositionAfterCorrectPosition() {
        val correctWord = "maxim"
        val guessWord = "magma"
        val userGuessWord = GuessWord(
            letters = List(5) {
                GuessLetter(guessWord[it])
            }.toImmutableList(),
            state = GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord)
        assertEquals(
            LetterState.Correct,
            updatedGuessWord.letters[0].state
        )
        assertEquals(
            LetterState.Present,
            updatedGuessWord.letters[3].state
        )
    }

    @Test
    fun duplicateCharacters_multipleIncorrectPositionsBeforeAndAfterCorrectPosition() {
        val correctWord = "maxim"
        val guessWord = "mummy"
        val userGuessWord = GuessWord(
            letters = List(5) {
                GuessLetter(guessWord[it])
            }.toImmutableList(),
            state = GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord)
        assertEquals(
            LetterState.Correct,
            updatedGuessWord.letters[0].state
        )
        assertEquals(
            LetterState.Present,
            updatedGuessWord.letters[3].state
        )
    }
}