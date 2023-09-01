package com.minutesock.core.data

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.minutesock.core.domain.GuessWord
import com.minutesock.core.remote.dto.Meaning
import com.minutesock.core.utils.JsonParser

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {
    @TypeConverter
    fun fromMeaningsJson(json: String): List<Meaning> {
        return jsonParser.fromJson<ArrayList<Meaning>>(
            json,
            object : TypeToken<ArrayList<Meaning>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toMeaningsJson(meanings: List<Meaning>): String {
        return jsonParser.toJson(
            meanings,
            object : TypeToken<ArrayList<Meaning>>() {}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromGuessWordJson(json: String): List<GuessWord> {
        return jsonParser.fromJson<ArrayList<GuessWord>>(
            json,
            object : TypeToken<ArrayList<GuessWord>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toGuessWordJson(guesses: List<GuessWord>): String {
        return jsonParser.toJson(
            guesses,
            object : TypeToken<ArrayList<GuessWord>>() {}.type
        ) ?: "[]"
    }
}