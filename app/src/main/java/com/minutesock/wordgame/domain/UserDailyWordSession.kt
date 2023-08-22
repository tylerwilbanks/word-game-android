package com.minutesock.wordgame.domain

import java.util.Date

data class UserDailyWordSession(
    val date: Date,
    val correctWord: String,
    val maxAttempts: Int
) {
    val wordLength = correctWord.length
}


