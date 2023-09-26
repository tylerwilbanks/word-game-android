package com.minutesock.core.presentation

import android.util.Log
import com.minutesock.core.R
import com.minutesock.core.data.repository.WordGameRepository
import com.minutesock.core.domain.DailyWordState
import com.minutesock.core.domain.DailyWordStateMessage
import com.minutesock.core.domain.DailyWordValidationResult
import com.minutesock.core.domain.DailyWordValidationResultType
import com.minutesock.core.domain.GuessKeyboardLetter
import com.minutesock.core.domain.GuessLetter
import com.minutesock.core.domain.GuessWord
import com.minutesock.core.domain.GuessWordState
import com.minutesock.core.domain.GuessWordValidator
import com.minutesock.core.domain.LetterState
import com.minutesock.core.domain.WordGameMode
import com.minutesock.core.domain.WordGameState
import com.minutesock.core.domain.WordScreenState
import com.minutesock.core.domain.WordSession
import com.minutesock.core.domain.addGuessLetter
import com.minutesock.core.domain.eraseLetter
import com.minutesock.core.domain.lockInGuess
import com.minutesock.core.domain.updateState
import com.minutesock.core.uiutils.UiText
import com.minutesock.core.utils.Option
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import java.util.Date

class WordGameLogicHelper(
    private val wordGameRepository: WordGameRepository
) {
    private val _state = MutableStateFlow(DailyWordState())
    val state = _state.asStateFlow()

    private var wordSession = WordSession(
        date = Date(),
        correctWord = state.value.correctWord
            ?: "",
        maxAttempts = state.value.maxGuessAttempts,
        guesses = state.value.guessWords.toImmutableList(),
        isDaily = true,
        gameState = state.value.gameState,
        startTime = Clock.System.now()
    )

    private var dailyWordStateMessage = DailyWordStateMessage()
    private var gameHasAlreadyBeenPlayed = false

    private fun getUpdatedFalseKeyboardKeys(
        guessWords: List<GuessWord>,
        falseKeyboardKeys: FalseKeyboardKeys
    ): FalseKeyboardKeys {
        val keys = hashMapOf<Char, LetterState>()
        guessWords.forEach { guessWord ->
            guessWord.letters.forEach { guessLetter ->
                val maxOrdinal = keys.filter {
                    it.key == guessLetter.character && it.value.ordinal > guessLetter.state.ordinal
                }.values.maxOfOrNull { it.ordinal }
                val newState = if (maxOrdinal != null) {
                    LetterState.values()[maxOrdinal]
                } else {
                    guessLetter.state
                }
                keys[guessLetter.character] = newState
            }
        }
        val keysWithNewState =
            keys.map { GuessKeyboardLetter(it.key.toString(), it.value) }.toImmutableList()
        val row1 = getUpdatedKeyboardRow(keysWithNewState, falseKeyboardKeys.row1)
        val row2 = getUpdatedKeyboardRow(keysWithNewState, falseKeyboardKeys.row2)
        val row3 = getUpdatedKeyboardRow(keysWithNewState, falseKeyboardKeys.row3)
        return FalseKeyboardKeys(row1, row2, row3)
    }

    private suspend fun initDailyWordSessionAndState(gameMode: WordGameMode) {
        withContext(Dispatchers.IO) {
            when (gameMode) {
                WordGameMode.Daily -> {
                    wordSession = wordGameRepository.loadDailySession(Date()) ?: WordSession(
                        date = Date(),
                        correctWord = state.value.correctWord ?: "",
                        maxAttempts = state.value.maxGuessAttempts,
                        guesses = state.value.guessWords.toImmutableList(),
                        isDaily = true,
                        gameState = state.value.gameState,
                        startTime = Clock.System.now()
                    )
                }

                WordGameMode.Inifinity -> {
                    wordSession = wordGameRepository.loadLatestToDoInfinitySession() ?: WordSession(
                        date = Date(),
                        correctWord = state.value.correctWord ?: "",
                        maxAttempts = state.value.maxGuessAttempts,
                        guesses = state.value.guessWords.toImmutableList(),
                        isDaily = false,
                        gameState = state.value.gameState,
                        startTime = Clock.System.now()
                    )
                }
            }

            _state.update {
                it.copy(
                    gameState = wordSession.gameState,
                    screenState = WordScreenState.Game,
                    wordLength = wordSession.correctWord.length,
                    maxGuessAttempts = wordSession.maxAttempts,
                    correctWord = wordSession.correctWord,
                    dailyWordStateMessage = DailyWordStateMessage(
                        uiText = UiText.StringResource(R.string.what_in_da_word)
                    ),
                    guessWords = wordSession.guesses,
                    falseKeyboardKeys = getUpdatedFalseKeyboardKeys(
                        wordSession.guesses,
                        state.value.falseKeyboardKeys
                    ),
                    wordRowAnimating = wordSession.gameState.isGameOver,
                    gameMode = if (wordSession.isDaily) WordGameMode.Daily else WordGameMode.Inifinity
                )
            }
        }
    }

    private suspend fun updateDailyWordSession(wordGameMode: WordGameMode = state.value.gameMode) {
        when (wordGameMode) {
            WordGameMode.Daily -> {
                wordGameRepository.saveWordSession(
                    wordSession = wordSession.copy(
                        correctWord = state.value.correctWord ?: "",
                        maxAttempts = state.value.maxGuessAttempts,
                        guesses = state.value.guessWords,
                        gameState = state.value.gameState
                    )
                )

                wordSession = wordGameRepository.loadDailySession(Date())!!
            }

            WordGameMode.Inifinity -> {
                wordGameRepository.saveWordSession(
                    wordSession = wordSession.copy(
                        correctWord = state.value.correctWord ?: "",
                        maxAttempts = state.value.maxGuessAttempts,
                        guesses = state.value.guessWords,
                        gameState = state.value.gameState
                    )
                )

                if (state.value.gameState.isGameOver) {
                    wordSession = wordGameRepository.loadInfinitySession(wordSession.id)!!
                } else {
                    wordSession = wordGameRepository.loadLatestToDoInfinitySession()!!

                }
            }
        }

    }

    private fun getUpdatedKeyboardRow(
        keysWithNewState: ImmutableList<GuessKeyboardLetter>,
        row: ImmutableList<GuessKeyboardLetter>
    ): ImmutableList<GuessKeyboardLetter> {
        val mutableRow = row.toMutableList()
        row.forEachIndexed { index, guessKey ->
            val a = keysWithNewState.firstOrNull { it.keyName == guessKey.keyName }
            mutableRow[index] = a ?: guessKey
        }
        return mutableRow.toImmutableList()
    }

    suspend fun setupGame(
        wordGameMode: WordGameMode,
        wordLength: Int = 5,
        maxGuessAttempts: Int = 6
    ) {
        withContext(Dispatchers.IO) {
            initDailyWordSessionAndState(wordGameMode)
            if (state.value.gameState == WordGameState.NotStarted) {
                setupNewGame(wordGameMode, wordLength, maxGuessAttempts)
            } else if (state.value.gameState.isGameOver) {
                gameHasAlreadyBeenPlayed = true
            }
            state.value.correctWord?.let {
                getOrFetchWordDefinition(it)
            }
            Log.e("shovel", "Correct word: ${state.value.correctWord}")
        }
    }

    private suspend fun setupNewGame(
        wordGameMode: WordGameMode,
        wordLength: Int = 5,
        maxGuessAttempts: Int = 6
    ) {
        val correctWord = GuessWordValidator.obtainRandomWord(state.value.gameMode)
        val w = List(maxGuessAttempts) {
            GuessWord(
                List(wordLength) {
                    GuessLetter()
                }.toImmutableList()
            )
        }.toMutableList()
        w[0] = w[0].updateState(GuessWordState.Editing)

        _state.update {
            DailyWordState(
                gameState = WordGameState.InProgress,
                wordLength = wordLength,
                maxGuessAttempts = maxGuessAttempts,
                correctWord = correctWord,
                guessWords = w.toImmutableList()
            )
        }
        if (wordGameMode == WordGameMode.Daily) {
            wordGameRepository.deleteDailySession(Date())
        }
        updateDailyWordSession(wordGameMode)

        _state.update { dailyWordState ->
            dailyWordState.copy(
                screenState = WordScreenState.Game,
                dailyWordStateMessage = DailyWordStateMessage(
                    uiText = UiText.StringResource(R.string.what_in_da_word)
                ),
            )
        }
    }

    private suspend fun getOrFetchWordDefinition(word: String) {
        withContext(Dispatchers.IO) {
            wordGameRepository.getOrFetchWordDefinition(word).onEach { option ->
                when (option) {
                    is Option.Error -> {}
                    is Option.Loading -> {
                        _state.update {
                            it.copy(
                                wordInfos = option.data?.toImmutableList() ?: persistentListOf()
                            )
                        }
                    }

                    is Option.Success -> {
                        _state.update {
                            it.copy(
                                wordInfos = option.data?.toImmutableList() ?: persistentListOf()
                            )
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    private fun getUpdatedWordRows(index: Int, guessWord: GuessWord): ImmutableList<GuessWord> {
        val mut = mutableListOf<GuessWord>()
        mut.addAll(state.value.guessWords)
        mut[index] = guessWord
        return mut.toImmutableList()
    }

    private suspend fun runItThroughThePipes(
        index: Int,
        guessWord: GuessWord
    ): ImmutableList<GuessWord> {
        val newDailyWordSession = wordSession.copy(
            gameState = state.value.gameState,
            guesses = getUpdatedWordRows(index, guessWord)
        )
        wordGameRepository.saveWordSession(newDailyWordSession)
        return when (state.value.gameMode) {
            WordGameMode.Daily -> {
                wordGameRepository.loadDailySession(newDailyWordSession.date)?.guesses
                    ?: persistentListOf()
            }

            WordGameMode.Inifinity -> {
                wordGameRepository.loadLatestToDoInfinitySession()?.guesses ?: persistentListOf()
            }
        }

    }

    suspend fun onGameEvent(event: WordEventGame) {
        if (state.value.gameState == WordGameState.NotStarted
        ) {
            return
        }
        when (event) {
            is WordEventGame.OnCharacterPress -> {
                if (state.value.gameState.isGameOver) {
                    return
                }
                withContext(Dispatchers.IO) {
                    getCurrentGuessWordIndexAndHandleError()?.let { index ->
                        updateCurrentGuessWord(index, event.character)
                    }
                }
            }

            WordEventGame.OnDeletePress -> {
                if (state.value.gameState.isGameOver) {
                    return
                }
                withContext(Dispatchers.IO) {
                    getCurrentGuessWordIndexAndHandleError()?.let { index ->
                        eraseLetter(index)
                    }
                }
            }

            WordEventGame.OnEnterPress -> {
                if (state.value.gameState.isGameOver) {
                    return
                }
                withContext(Dispatchers.IO) {
                    getCurrentGuessWordIndexAndHandleError()?.let { index ->
                        val currentGuessWord = state.value.guessWords[index]
                        val result = GuessWordValidator.validateGuess(
                            guessWord = currentGuessWord,
                            correctWord = state.value.correctWord!!,
                            isFinalGuess = isFinalGuess(index)
                        )
                        when (result.type) {
                            DailyWordValidationResultType.Unknown -> {}
                            DailyWordValidationResultType.Error -> {
                                displayError(result.uiText)
                            }

                            DailyWordValidationResultType.Incorrect -> {
                                dailyWordStateMessage = DailyWordStateMessage(
                                    uiText = result.uiText,
                                    isError = isFinalGuess(index)
                                )
                                val updatedGuessWords = runItThroughThePipes(
                                    index,
                                    state.value.guessWords[index].lockInGuess(
                                        state.value.correctWord!!,
                                        isFinalGuess(index)
                                    )
                                )
                                _state.update {
                                    it.copy(
                                        wordRowAnimating = true,
                                        guessWords = updatedGuessWords,
                                        falseKeyboardKeys = getUpdatedFalseKeyboardKeys(
                                            updatedGuessWords,
                                            state.value.falseKeyboardKeys
                                        ),
                                    )
                                }
                                if (isFinalGuess(index)) {
                                    onGameFinishedFailure()
                                } else {
                                    _state.update {
                                        it.copy(
                                            guessWords = runItThroughThePipes(
                                                index + 1, state.value.guessWords[index + 1].copy(
                                                    state = GuessWordState.Editing
                                                )
                                            )
                                        )
                                    }
                                }

                            }

                            DailyWordValidationResultType.Success -> {
                                onGameFinishedSuccess(index, result)
                            }
                        }
                    }
                }
            }

            WordEventGame.OnErrorAnimationFinished -> {
                if (state.value.gameState.isGameOver) {
                    return
                }
                withContext(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            dailyWordStateMessage = DailyWordStateMessage(
                                uiText = UiText.StringResource(R.string.what_in_da_word)
                            )
                        )
                    }
                }
            }

            WordEventGame.OnAnsweredWordRowAnimationFinished -> {
                withContext(Dispatchers.IO) {
                    if (
                        state.value.gameState.isGameOver &&
                        !state.value.guessWords.any { it.state == GuessWordState.Correct || it.state == GuessWordState.Failure }
                    ) {
                        state.value.guessWords.indexOfLast { it.state == GuessWordState.Complete }
                            .let { index ->
                                _state.update {
                                    it.copy(
                                        guessWords = runItThroughThePipes(
                                            index, state.value.guessWords[index].updateState(
                                                if (state.value.gameState == WordGameState.Success) {
                                                    GuessWordState.Correct
                                                } else {
                                                    GuessWordState.Failure
                                                }
                                            )
                                        )
                                    )
                                }
                            }
                    }

                    _state.update {
                        it.copy(
                            wordRowAnimating = false,
                            dailyWordStateMessage = if (dailyWordStateMessage.uiText == null) {
                                DailyWordStateMessage(
                                    uiText = GuessWordValidator.obtainRandomMessageBasedOnGameState(
                                        state.value.gameState
                                    )
                                )
                            } else {
                                dailyWordStateMessage
                            }

                        )
                    }
                }
            }

            WordEventGame.OnCompleteAnimationFinished -> {
                if (gameHasAlreadyBeenPlayed) {
                    return
                }
                _state.update {
                    it.copy(
                        screenState = WordScreenState.Stats
                    )
                }
            }

            WordEventGame.OnStatsPress -> _state.update {
                it.copy(
                    screenState = WordScreenState.Stats
                )
            }
        }
    }

    suspend fun onStatsEvent(event: WordEventStats) {
        when (event) {
            WordEventStats.OnExitButtonPressed -> {
                _state.update {
                    it.copy(
                        screenState = WordScreenState.Game
                    )
                }
            }

            WordEventStats.OnShareButtonPressed -> {
                _state.update {
                    it.copy(
                        shareText = wordSession.shareText
                    )
                }
            }

            WordEventStats.OnShareChooserPresented -> {
                _state.update {
                    it.copy(
                        shareText = null
                    )
                }
            }

            WordEventStats.OnDeleteAndRestartSessionPressed -> {
                withContext(Dispatchers.IO) {
                    gameHasAlreadyBeenPlayed = false
                    val wordLength = wordSession.wordLength
                    val maxAttempts = wordSession.maxAttempts
                    wordGameRepository.deleteDailySession(Date())
                    _state.update {
                        DailyWordState(
                            gameMode = WordGameMode.Daily,
                            gameState = WordGameState.NotStarted
                        )
                    }
                    setupGame(WordGameMode.Daily, wordLength, maxAttempts)
                }
            }

            WordEventStats.OnInfinityNextSessionPressed -> {
                withContext(Dispatchers.IO) {
                    gameHasAlreadyBeenPlayed = false
                    val wordLength = wordSession.wordLength
                    val maxAttempts = wordSession.maxAttempts
                    _state.update {
                        DailyWordState(
                            gameMode = WordGameMode.Inifinity,
                            gameState = WordGameState.NotStarted
                        )
                    }
                    setupGame(WordGameMode.Inifinity, wordLength, maxAttempts)
                }
            }
        }
    }

    private suspend fun onGameFinishedFailure() {
        withContext(Dispatchers.IO) {
            _state.update {
                it.copy(
                    wordRowAnimating = true,
                    gameState = WordGameState.Failure,
                    falseKeyboardKeys = getUpdatedFalseKeyboardKeys(
                        state.value.guessWords,
                        state.value.falseKeyboardKeys
                    )
                )
            }
            updateDailyWordSession()
        }
    }

    private suspend fun onGameFinishedSuccess(
        userGuessWordIndex: Int,
        dailyWordValidationResult: DailyWordValidationResult
    ) {
        dailyWordStateMessage = DailyWordStateMessage(
            uiText = dailyWordValidationResult.uiText,
            isError = false
        )
        val updatedGuessWords = getUpdatedWordRows(
            userGuessWordIndex,
            state.value.guessWords[userGuessWordIndex].lockInGuess(
                state.value.correctWord!!,
                isFinalGuess(userGuessWordIndex)
            )
        )
        _state.update {
            it.copy(
                wordRowAnimating = true,
                gameState = WordGameState.Success,
                guessWords = updatedGuessWords,
                falseKeyboardKeys = getUpdatedFalseKeyboardKeys(
                    updatedGuessWords,
                    state.value.falseKeyboardKeys
                ),
            )
        }
        updateDailyWordSession()
    }

    private fun isFinalGuess(index: Int): Boolean = index + 1 == state.value.guessWords.size

    private fun displayError(
        uiText: UiText,
        guessWordError: GuessWordError? = null
    ) {
        _state.update {
            it.copy(
                dailyWordStateMessage = DailyWordStateMessage(
                    uiText = uiText,
                    isError = true
                )
            )
        }

        getCurrentGuessWordIndexAndHandleError()?.let { index ->
            _state.update {
                it.copy(
                    guessWords = getUpdatedWordRows(
                        index, state.value.guessWords[index].copy(
                            errorState = guessWordError ?: GuessWordError.Unknown
                        )
                    )
                )
            }
        }
    }

    private fun getCurrentGuessWordIndex(): Option<Int> {
        val index = state.value.guessWords.indexOfFirst {
            it.state == GuessWordState.Editing
        }
        return if (index == -1) {
            Option.Error(
                uiText = UiText.StringResource(R.string.there_is_no_more_guess_attempts_left)
            )
        } else {
            Option.Success(index)
        }
    }

    private fun getCurrentGuessWordIndexAndHandleError(): Int? {
        return when (val result = getCurrentGuessWordIndex()) {
            is Option.Error -> {
                result.uiText?.let { uiText ->
                    _state.update {
                        it.copy(
                            dailyWordStateMessage = DailyWordStateMessage(
                                uiText = uiText,
                                isError = true
                            )
                        )
                    }
                }
                null
            }

            is Option.Success -> result.data

            else -> {
                /* ignore Option.Error */
                null
            }
        }
    }

    private fun updateCurrentGuessWord(index: Int, character: Char) {
        val result = state.value.guessWords[index].addGuessLetter(
            GuessLetter(_character = character)
        )
        when (result) {
            is Option.Error -> {
                result.uiText?.let { uiTextError ->
                    result.errorCode?.let { errorCode ->
                        _state.update {
                            it.copy(
                                guessWords = getUpdatedWordRows(
                                    index, state.value.guessWords[index].copy(
                                        errorState = GuessWordError.values()[errorCode]
                                    )
                                )
                            )
                        }

                    }
                    _state.update {
                        it.copy(
                            dailyWordStateMessage = DailyWordStateMessage(
                                uiText = uiTextError,
                                isError = true
                            )
                        )
                    }
                }

            }

            is Option.Success -> {
                result.data?.let { newGuessWord ->
                    _state.update {
                        it.copy(
                            guessWords = getUpdatedWordRows(index, newGuessWord)
                        )
                    }
                }
            }

            else -> {
                /* ignore Option.Error */
            }
        }
    }

    private fun eraseLetter(index: Int) {
        when (val result = state.value.guessWords[index].eraseLetter()) {
            is Option.Error -> {
                result.uiText?.let { uiText ->
                    result.errorCode?.let { errorCode ->
                        _state.update {
                            it.copy(
                                guessWords = getUpdatedWordRows(
                                    index, state.value.guessWords[index].copy(
                                        errorState = GuessWordError.values()[errorCode]
                                    )
                                )
                            )
                        }
                    }
                    _state.update {
                        it.copy(
                            dailyWordStateMessage = DailyWordStateMessage(
                                uiText = uiText,
                                isError = true
                            )
                        )
                    }
                }

            }

            is Option.Success -> {
                result.data?.let { guessWord ->
                    _state.update {
                        it.copy(
                            guessWords = getUpdatedWordRows(index, guessWord)
                        )
                    }
                }
            }

            else -> {/* ignore Option.Error */
            }
        }
    }
}
