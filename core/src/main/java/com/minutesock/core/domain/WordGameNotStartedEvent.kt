package com.minutesock.core.domain

sealed class WordGameNotStartedEvent {
    data class OnGameBegin(
        val gameMode: WordGameMode,
        val wordLength: Int = 5,
        val maxAttempts: Int = 6
    ) : WordGameNotStartedEvent()
}
