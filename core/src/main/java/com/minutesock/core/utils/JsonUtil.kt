package com.minutesock.core.utils

import org.json.JSONArray

fun JSONArray.convertToStringList(): List<String> {
    val stringList = mutableListOf<String>()

    for (i in 0 until this.length()) {
        stringList.add(this.getString(i))
    }
    return stringList
}