package com.minutesock.wordgame

import com.minutesock.wordgame.domain.GuessWordState
import com.minutesock.wordgame.domain.UserGuessLetter
import com.minutesock.wordgame.domain.UserGuessWord
import com.minutesock.wordgame.domain.UserLetterState
import com.minutesock.wordgame.domain.lockInGuess
import junit.framework.TestCase.assertEquals
import kotlinx.collections.immutable.toImmutableList
import org.junit.Test

class UserGuessWordUnitTest {

    @Test
    fun duplicateCharacters_singleIncorrectPositionBeforeCorrectPosition() {
        val correctWord = "cedar"
        val guessWord = "radar"
        val userGuessWord = UserGuessWord(
            letters = List(5) {
                UserGuessLetter(guessWord[it])
            }.toImmutableList(),
            state = GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord)
        assertEquals(UserLetterState.Absent, updatedGuessWord.letters.first().state)
    }

    @Test
    fun duplicateCharacters_multipleIncorrectPositionsBeforeCorrectPosition() {
        val correctWord = "razor"
        val guessWord = "error"
        val userGuessWord = UserGuessWord(
            letters = List(5) {
                UserGuessLetter(guessWord[it])
            }.toImmutableList(),
            state = GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord)
        assertEquals(UserLetterState.Absent, updatedGuessWord.letters[1].state)
        assertEquals(UserLetterState.Present, updatedGuessWord.letters[2].state)
        assertEquals(UserLetterState.Correct, updatedGuessWord.letters[4].state)
    }

    @Test
    fun duplicateCharacters_singleIncorrectPositionAfterCorrectPosition() {
        val correctWord = "maxim"
        val guessWord = "magma"
        val userGuessWord = UserGuessWord(
            letters = List(5) {
                UserGuessLetter(guessWord[it])
            }.toImmutableList(),
            state = GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord)
        assertEquals(UserLetterState.Correct, updatedGuessWord.letters[0].state)
        assertEquals(UserLetterState.Present, updatedGuessWord.letters[3].state)
    }

    @Test
    fun duplicateCharacters_multipleIncorrectPositionsBeforeAndAfterCorrectPosition() {
        val correctWord = "maxim"
        val guessWord = "mummy"
        val userGuessWord = UserGuessWord(
            letters = List(5) {
                UserGuessLetter(guessWord[it])
            }.toImmutableList(),
            state = GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord)
        assertEquals(UserLetterState.Correct, updatedGuessWord.letters[0].state)
        assertEquals(UserLetterState.Present, updatedGuessWord.letters[3].state)
    }
}