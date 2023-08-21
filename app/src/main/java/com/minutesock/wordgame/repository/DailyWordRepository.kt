package com.minutesock.wordgame.repository

import com.minutesock.wordgame.remote.DictionaryApi
import com.minutesock.wordgame.remote.RetrofitInstance
import com.minutesock.wordgame.remote.responses.WordDefinition
import com.minutesock.wordgame.uiutils.UiText
import com.minutesock.wordgame.utils.Option
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DailyWordRepository(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dictionaryApi: DictionaryApi = RetrofitInstance.dictionaryApi
) {

    suspend fun fetchWordDefinition(word: String): Option<WordDefinition> {
        return withContext(defaultDispatcher) {
            return@withContext try {
                val response = dictionaryApi.fetchWordDefinition(word)
                if (response.isSuccessful) {
                    return@withContext Option.Success(response.body())
                }
                return@withContext Option.UiError(
                    uiText = UiText.DynamicString(
                        response.message()
                            .ifBlank { "An error occurred fetching the word definition" })
                )
            } catch (e: Exception) {
                Option.Error(e.message ?: "An error occurred fetching the word definition")
            }
        }
    }
}