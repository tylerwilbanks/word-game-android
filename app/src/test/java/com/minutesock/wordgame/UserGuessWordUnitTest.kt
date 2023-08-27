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
    fun duplicateCharacters_single_isAbsent() {
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
    fun duplicateCharacters_multipleIncorrectPositions_isPresent() {
        val correctWord = "razor"
        val guessWord = "error"
        val userGuessWord = UserGuessWord(
            letters = List(5) {
                UserGuessLetter(guessWord[it])
            }.toImmutableList(),
            state = GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord)
        assertEquals(UserLetterState.Present, updatedGuessWord.letters[1].state)
        assertEquals(UserLetterState.Absent, updatedGuessWord.letters[2].state)
        assertEquals(UserLetterState.Correct, updatedGuessWord.letters[4].state)
    }
}