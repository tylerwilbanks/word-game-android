package com.minutesock.wordgame.web

import com.minutesock.wordgame.web.data.WordDefinition
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    @GET("entries/en/{word}")
    suspend fun fetchWordDefinition(@Path("word") word: String): Response<WordDefinition>
}