package com.minutesock.core.presentation

sealed interface WordEventStats {
    object OnExitButtonPressed : WordEventStats
    object OnShareButtonPressed : WordEventStats
    object OnShareChooserPresented : WordEventStats
    object OnDeleteAndRestartSessionPressed : WordEventStats
}