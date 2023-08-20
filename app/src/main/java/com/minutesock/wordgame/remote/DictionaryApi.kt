package com.minutesock.wordgame.remote

import com.minutesock.wordgame.remote.responses.WordDefinition
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    @GET("entries/en/{word}")
    suspend fun fetchWordDefinition(@Path("word") word: String): Response<WordDefinition>
}