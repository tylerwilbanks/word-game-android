package com.minutesock.core.domain

sealed interface WordEventStats {
    object OnExitButtonPressed : WordEventStats
    object OnShareButtonPressed : WordEventStats
    object OnShareChooserPresented : WordEventStats
    object OnDeleteAndRestartSessionPressed : WordEventStats
    object OnInfinityNextSessionPressed : WordEventStats
}