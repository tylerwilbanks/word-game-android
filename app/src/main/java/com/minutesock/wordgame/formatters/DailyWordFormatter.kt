package com.minutesock.wordgame.formatters

import android.os.Build
import com.minutesock.wordgame.data.DatabaseInstance
import com.minutesock.wordgame.data.models.DailyWordSession
import com.minutesock.wordgame.domain.UserDailyWordSession
import com.minutesock.wordgame.utils.toDate
import com.minutesock.wordgame.utils.toString
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun DailyWordSession.toUserDailyWordSession(): UserDailyWordSession {
    return UserDailyWordSession(
        date = this.date.toDate(DailyWordFormatter.DATE_FORMAT_PATTERN),
        correctWord = this.correctWord,
        maxAttempts = this.maxAttempts
    )
}

fun UserDailyWordSession.toDailyWordSession(): DailyWordSession {
    return DailyWordSession(
        id = 0,
        date = SimpleDateFormat(DailyWordFormatter.DATE_FORMAT_PATTERN, Locale.getDefault()).format(this.date),
        correctWord = this.correctWord,
        maxAttempts = this.maxAttempts
    )
}

class DailyWordFormatter {

    // todo clean this up
    fun getTodaySession(userDailyWordSession: UserDailyWordSession): UserDailyWordSession {
        val session = queryForTodaySession(userDailyWordSession)
        return if (session == null) {
            DatabaseInstance.db.DailyWordSessionDao().insert(userDailyWordSession.toDailyWordSession())
            queryForTodaySession(userDailyWordSession)?.toUserDailyWordSession()!!
        } else {
            session.toUserDailyWordSession()
        }
    }

    private fun queryForTodaySession(userDailyWordSession: UserDailyWordSession): DailyWordSession? {
        return DatabaseInstance.db.DailyWordSessionDao().getTodaySession(
            userDailyWordSession.date.toString(
                DATE_FORMAT_PATTERN
            )
        )
    }

    companion object {
        const val DATE_FORMAT_PATTERN = "dd/MM/yyyy"
    }
}