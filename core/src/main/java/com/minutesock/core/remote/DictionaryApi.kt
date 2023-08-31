package com.minutesock.core.remote

import com.minutesock.core.remote.dto.WordDefinition
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    @GET("entries/en/{word}")
    suspend fun fetchWordDefinition(@Path("word") word: String): Response<WordDefinition>

    companion object {
        const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/"
    }
}