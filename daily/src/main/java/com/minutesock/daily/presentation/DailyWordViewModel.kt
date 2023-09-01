package com.minutesock.daily.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.core.App
import com.minutesock.core.domain.DailyWordGameState
import com.minutesock.core.domain.DailyWordScreenState
import com.minutesock.core.domain.DailyWordSession
import com.minutesock.core.domain.DailyWordState
import com.minutesock.core.domain.DailyWordStateMessage
import com.minutesock.core.domain.GuessKeyboardLetter
import com.minutesock.core.domain.GuessLetter
import com.minutesock.core.domain.GuessWord
import com.minutesock.core.domain.GuessWordState
import com.minutesock.core.domain.LetterState
import com.minutesock.core.domain.addGuessLetter
import com.minutesock.core.domain.eraseLetter
import com.minutesock.core.domain.lockInGuess
import com.minutesock.core.domain.updateState
import com.minutesock.core.presentation.FalseKeyboardKeys
import com.minutesock.core.presentation.GuessWordError
import com.minutesock.core.uiutils.UiText
import com.minutesock.core.utils.Option
import com.minutesock.daily.R
import com.minutesock.daily.data.repository.DailyWordRepository
import com.minutesock.daily.domain.DailyWordValidationResult
import com.minutesock.daily.domain.DailyWordValidationResultType
import com.minutesock.daily.domain.GuessWordValidator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class DailyWordViewModel(
    private val dailyWordRepository: DailyWordRepository = DailyWordRepository(
        wordInfoDao = App.database.WordInfoDao(),
        dailyWordSessionDao = App.database.DailyWordSessionDao()
    )
) : ViewModel() {

    private val _state = MutableStateFlow(DailyWordState())
    val state = _state.asStateFlow()

    val guessWords = mutableStateListOf<GuessWord>()

    private var dailyWordStateMessage = DailyWordStateMessage()

    init {
        updateDailyWordStateWithSavedDailySession()
    }

    private fun updateDailyWordStateWithSavedDailySession() {
        viewModelScope.launch {
            dailyWordRepository.loadDailySession(Date())?.let { dailyWordSession ->
                _state.update {
                    it.copy(
                        gameState = dailyWordSession.gameState,
                        screenState = DailyWordScreenState.Game,
                        wordLength = dailyWordSession.correctWord.length,
                        maxGuessAttempts = dailyWordSession.maxAttempts,
                        correctWord = dailyWordSession.correctWord,
                        dailyWordStateMessage = DailyWordStateMessage(
                            uiText = UiText.StringResource(R.string.what_in_da_word)
                        )
                    )
                }
            }
        }
    }

    private val falseKeyboardKeys: FalseKeyboardKeys
        get() {
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
            val row1 = getUpdatedKeyboardRow(keysWithNewState, state.value.falseKeyboardKeys.row1)
            val row2 = getUpdatedKeyboardRow(keysWithNewState, state.value.falseKeyboardKeys.row2)
            val row3 = getUpdatedKeyboardRow(keysWithNewState, state.value.falseKeyboardKeys.row3)
            return FalseKeyboardKeys(row1, row2, row3)
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

    fun setupGame(wordLength: Int = 5, maxGuessAttempts: Int = 6) {
        if (state.value.gameState == DailyWordGameState.NotStarted) {
            val w = List(maxGuessAttempts) {
                GuessWord(
                    List(wordLength) {
                        GuessLetter()
                    }.toImmutableList()
                )
            }.toMutableList()
            w[0] = w[0].updateState(GuessWordState.Editing)
            guessWords.addAll(w)
            val correctWord = GuessWordValidator.obtainRandomWord()
            _state.update { dailyWordState ->
                dailyWordState.copy(
                    gameState = DailyWordGameState.InProgress,
                    screenState = DailyWordScreenState.Game,
                    wordLength = wordLength,
                    maxGuessAttempts = maxGuessAttempts,
                    correctWord = correctWord,
                    dailyWordStateMessage = DailyWordStateMessage(
                        uiText = UiText.StringResource(R.string.what_in_da_word)
                    )
                )
            }
            getOrFetchWordDefinition(correctWord)
        }
    }

    private fun getOrFetchWordDefinition(word: String) {
        viewModelScope.launch {
            dailyWordRepository.getOrFetchWordDefinition(word).onEach { option ->
                when (option) {
                    is Option.Error -> { /* todo-tyler handle error */
                    }

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

    fun onGameEvent(event: DailyWordEventGame) {
        if (state.value.gameState == DailyWordGameState.NotStarted
        ) {
            return
        }
        when (event) {
            is DailyWordEventGame.OnCharacterPress -> {
                if (state.value.gameState.isGameOver) {
                    return
                }
                viewModelScope.launch {
                    getCurrentGuessWordIndexAndHandleError()?.let { index ->
                        updateCurrentGuessWord(index, event.character)
                    }
                }
            }

            DailyWordEventGame.OnDeletePress -> {
                if (state.value.gameState.isGameOver) {
                    return
                }
                viewModelScope.launch {
                    getCurrentGuessWordIndexAndHandleError()?.let { index ->
                        eraseLetter(index)
                    }
                }
            }

            DailyWordEventGame.OnEnterPress -> {
                if (state.value.gameState.isGameOver) {
                    return
                }
                viewModelScope.launch {
                    getCurrentGuessWordIndexAndHandleError()?.let { index ->
                        val currentGuessWord = guessWords[index]
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
                                guessWords[index] =
                                    guessWords[index].lockInGuess(state.value.correctWord!!)
                                dailyWordStateMessage = DailyWordStateMessage(
                                    uiText = result.uiText,
                                    isError = isFinalGuess(index)
                                )
                                _state.update {
                                    it.copy(
                                        wordRowAnimating = true,
                                        falseKeyboardKeys = falseKeyboardKeys
                                    )
                                }

                                if (isFinalGuess(index)) {
                                    onGameFinishedFailure()
                                } else {
                                    guessWords[index + 1] = guessWords[index + 1].copy(
                                        state = GuessWordState.Editing
                                    )
                                }

                            }

                            DailyWordValidationResultType.Success -> {
                                onGameFinishedSuccess(index, result)
                            }
                        }
                    }
                }
            }

            DailyWordEventGame.OnErrorAnimationFinished -> {
                if (state.value.gameState.isGameOver) {
                    return;
                }
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            dailyWordStateMessage = DailyWordStateMessage(
                                uiText = UiText.StringResource(R.string.what_in_da_word)
                            )
                        )
                    }
                }
            }

            DailyWordEventGame.OnAnsweredWordRowAnimationFinished -> {
                viewModelScope.launch {
                    if (state.value.gameState.isGameOver) {
                        guessWords.indexOfLast { it.state == GuessWordState.Complete }
                            .let { index ->
                                guessWords[index] =
                                    guessWords[index].updateState(
                                        if (state.value.gameState == DailyWordGameState.Success) {
                                            GuessWordState.Correct

                                        } else {
                                            GuessWordState.Failure
                                        }
                                    )
                            }
                    }
                    _state.update {
                        it.copy(
                            wordRowAnimating = false,
                            dailyWordStateMessage = dailyWordStateMessage
                        )
                    }
                }
            }

            DailyWordEventGame.OnCompleteAnimationFinished -> {
                _state.update {
                    it.copy(
                        screenState = DailyWordScreenState.Stats
                    )
                }
            }

            DailyWordEventGame.OnStatsPress -> _state.update {
                it.copy(
                    screenState = DailyWordScreenState.Stats
                )
            }
        }
    }

    fun onStatsEvent(event: DailyWordEventStats) {
        when (event) {
            DailyWordEventStats.OnExitButtonPressed -> {
                _state.update {
                    it.copy(
                        screenState = DailyWordScreenState.Game
                    )
                }
            }

            DailyWordEventStats.OnShareButtonPressed -> {
                _state.update {
                    it.copy(
                        shareText = buildShareText()
                    )
                }
            }

            DailyWordEventStats.OnShareChooserPresented -> {
                _state.update {
                    it.copy(
                        shareText = null
                    )
                }
            }
        }
    }

    private suspend fun onGameFinishedFailure() {
        saveUserDailyWordSession()
        _state.update {
            it.copy(
                wordRowAnimating = true,
                gameState = DailyWordGameState.Failure,
                falseKeyboardKeys = falseKeyboardKeys
            )
        }
    }

    private suspend fun onGameFinishedSuccess(
        userGuessWordIndex: Int,
        dailyWordValidationResult: DailyWordValidationResult
    ) {
        guessWords[userGuessWordIndex] =
            guessWords[userGuessWordIndex].lockInGuess(state.value.correctWord!!)
        saveUserDailyWordSession()
        dailyWordStateMessage = DailyWordStateMessage(
            uiText = dailyWordValidationResult.uiText,
            isError = false
        )
        _state.update {
            it.copy(
                wordRowAnimating = true,
                falseKeyboardKeys = falseKeyboardKeys,
                gameState = DailyWordGameState.Success,
            )
        }
    }

    private suspend fun saveUserDailyWordSession() {
        withContext(Dispatchers.IO) {
            dailyWordRepository.saveDailySession(
                dailyWordSession = DailyWordSession(
                    date = Date(),
                    correctWord = state.value.correctWord
                        ?: "THE CORRECT WORD IS NULL FOR SOME REASON",
                    maxAttempts = state.value.maxGuessAttempts,
                    guesses = guessWords.toImmutableList(),
                    isDaily = true,
                    gameState = state.value.gameState
                )
            )
        }
    }

    private fun buildShareText(): String {
        val finalIndex =
            guessWords.indexOfFirst { it.state == GuessWordState.Correct || it.state == GuessWordState.Failure }
        val resultLetter =
            if (finalIndex + 1 >= guessWords.size && state.value.gameState == DailyWordGameState.Failure) "X" else "${finalIndex + 1}"
        var text = "$resultLetter/${guessWords.size}\n"
        guessWords.forEachIndexed { index, guessWord ->
            if (index > finalIndex) {
                return@forEachIndexed
            }
            guessWord.letters.forEach {
                text += it.state.emoji
            }
            text += "\n"
        }
        return text
    }

    private fun isFinalGuess(index: Int): Boolean = index + 1 == guessWords.size

    private fun displayError(
        uiText: com.minutesock.core.uiutils.UiText,
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
            guessWords[index] = guessWords[index].copy(
                errorState = guessWordError ?: GuessWordError.Unknown
            )
        }
    }

    private fun getCurrentGuessWordIndex(): Option<Int> {
        val index = guessWords.indexOfFirst {
            it.state == GuessWordState.Editing
        }
        return if (index == -1) {
            Option.Error(
                uiText = com.minutesock.core.uiutils.UiText.StringResource(R.string.there_is_no_more_guess_attempts_left)
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
        val result = guessWords[index].addGuessLetter(
            GuessLetter(_character = character)
        )
        when (result) {
            is Option.Error -> {
                result.uiText?.let { uiTextError ->
                    result.errorCode?.let { errorCode ->
                        guessWords[index] = guessWords[index].copy(
                            errorState = GuessWordError.values()[errorCode]
                        )
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
                result.data?.let {
                    guessWords[index] = it
                }
            }

            else -> {
                /* ignore Option.Error */
            }
        }
    }

    private fun eraseLetter(index: Int) {
        when (val result = guessWords[index].eraseLetter()) {
            is Option.Error -> {
                result.uiText?.let { uiText ->
                    result.errorCode?.let { errorCode ->
                        guessWords[index] = guessWords[index].copy(
                            errorState = GuessWordError.values()[errorCode]
                        )
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
                    guessWords[index] = guessWord
                }
            }

            else -> {/* ignore Option.Error */
            }
        }
    }
}