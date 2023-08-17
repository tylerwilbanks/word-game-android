package com.minutesock.wordgame.domain

enum class LetterState {
    Unknown,
    Absent,
    Present,
    Correct;

    val emoji: String
        get() {
            return when (this) {
                Unknown -> "⬛"
                Absent -> "⬛"
                Present -> "🟨"
                Correct -> "🟩"
            }
        }
}