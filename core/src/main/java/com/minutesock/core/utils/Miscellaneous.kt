package com.minutesock.core.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.capitalize(locale: Locale = Locale.ROOT): String {
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            locale
        ) else it.toString()
    }
}

fun String.toDate(dateFormat: String): Date {
    val format = SimpleDateFormat(dateFormat, Locale.getDefault())
    return try {
        format.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
        Date()
    }
}

fun Date.toString(dateFormat: String): String {
    val format = SimpleDateFormat(dateFormat, Locale.getDefault())
    return try {
        format.format(this)
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}

fun Long.asDaysToMillis(): Long = this * 24 * 60 * 60 * 1000
