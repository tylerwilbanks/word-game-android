package com.minutesock.core.data

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
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
    fun fromGuessWordStorageJson(json: String): List<GuessWordStorage> {
        return jsonParser.fromJson<ArrayList<GuessWordStorage>>(
            json,
            object : TypeToken<ArrayList<GuessWordStorage>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toGuessWordStorageJson(guesses: List<GuessWordStorage>): String {
        return jsonParser.toJson(
            guesses,
            object : TypeToken<ArrayList<GuessWordStorage>>() {}.type
        ) ?: "[]"
    }
}