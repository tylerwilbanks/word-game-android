package com.minutesock.wordgame

import com.minutesock.daily.domain.lockInGuess
import junit.framework.TestCase.assertEquals
import kotlinx.collections.immutable.toImmutableList
import org.junit.Test

class UserGuessWordUnitTest {

    @Test
    fun duplicateCharacters_singleIncorrectPositionBeforeCorrectPosition() {
        val correctWord = "cedar"
        val guessWord = "radar"
        val userGuessWord = com.minutesock.daily.domain.UserGuessWord(
            letters = List(5) {
                com.minutesock.daily.domain.UserGuessLetter(guessWord[it])
            }.toImmutableList(),
            state = com.minutesock.daily.domain.GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord)
        assertEquals(
            com.minutesock.daily.domain.UserLetterState.Absent,
            updatedGuessWord.letters.first().state
        )
    }

    @Test
    fun duplicateCharacters_multipleIncorrectPositionsBeforeCorrectPosition() {
        val correctWord = "razor"
        val guessWord = "error"
        val userGuessWord = com.minutesock.daily.domain.UserGuessWord(
            letters = List(5) {
                com.minutesock.daily.domain.UserGuessLetter(guessWord[it])
            }.toImmutableList(),
            state = com.minutesock.daily.domain.GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord)
        assertEquals(
            com.minutesock.daily.domain.UserLetterState.Absent,
            updatedGuessWord.letters[1].state
        )
        assertEquals(
            com.minutesock.daily.domain.UserLetterState.Present,
            updatedGuessWord.letters[2].state
        )
        assertEquals(
            com.minutesock.daily.domain.UserLetterState.Correct,
            updatedGuessWord.letters[4].state
        )
    }

    @Test
    fun duplicateCharacters_singleIncorrectPositionAfterCorrectPosition() {
        val correctWord = "maxim"
        val guessWord = "magma"
        val userGuessWord = com.minutesock.daily.domain.UserGuessWord(
            letters = List(5) {
                com.minutesock.daily.domain.UserGuessLetter(guessWord[it])
            }.toImmutableList(),
            state = com.minutesock.daily.domain.GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord)
        assertEquals(
            com.minutesock.daily.domain.UserLetterState.Correct,
            updatedGuessWord.letters[0].state
        )
        assertEquals(
            com.minutesock.daily.domain.UserLetterState.Present,
            updatedGuessWord.letters[3].state
        )
    }

    @Test
    fun duplicateCharacters_multipleIncorrectPositionsBeforeAndAfterCorrectPosition() {
        val correctWord = "maxim"
        val guessWord = "mummy"
        val userGuessWord = com.minutesock.daily.domain.UserGuessWord(
            letters = List(5) {
                com.minutesock.daily.domain.UserGuessLetter(guessWord[it])
            }.toImmutableList(),
            state = com.minutesock.daily.domain.GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord)
        assertEquals(
            com.minutesock.daily.domain.UserLetterState.Correct,
            updatedGuessWord.letters[0].state
        )
        assertEquals(
            com.minutesock.daily.domain.UserLetterState.Present,
            updatedGuessWord.letters[3].state
        )
    }
}