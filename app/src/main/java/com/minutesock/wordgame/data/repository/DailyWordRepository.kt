package com.minutesock.wordgame.data.repository

import com.minutesock.wordgame.data.WordInfoDao
import com.minutesock.wordgame.domain.WordInfo
import com.minutesock.wordgame.remote.DictionaryApi
import com.minutesock.wordgame.remote.RetrofitInstance
import com.minutesock.wordgame.remote.dto.WordDefinition
import com.minutesock.wordgame.uiutils.UiText
import com.minutesock.wordgame.utils.Option
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class DailyWordRepository(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dictionaryApi: DictionaryApi = RetrofitInstance.dictionaryApi,
    private val wordInfoDao: WordInfoDao
) {

    // todo complete this method
    suspend fun getOrFetchWordDefinition(word: String): Flow<Option<List<WordInfo>>> = flow {
        emit(Option.Loading())

        val data = wordInfoDao.getWordInfos(word).map { it.toWordInfo() }
        emit(Option.Loading(data = data))

        try {
            val response = dictionaryApi.fetchWordDefinition(word)
            if (response.isSuccessful) {
                response.body()?.let { remoteWordDefinitions ->
                    wordInfoDao.deleteWordInfos(remoteWordDefinitions.map { it.word })
                    wordInfoDao.insertWordInfos(remoteWordDefinitions.map { it.toWordInfoEntity() })
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val message = when (e) {
                is HttpException -> "Something went wrong!"
                is IOException -> "Couldn't reach server. Please check your internet connection."
                else -> {
                    "An error occurred fetching the word definition"
                }
            }
            emit(
                Option.Error(
                    uiText = UiText.DynamicString(message),
                    message = e.message ?: message
                )
            )
        }

        val newWordInfos = wordInfoDao.getWordInfos(word).map { it.toWordInfo() }
        emit(Option.Success(newWordInfos))
    }

    @Deprecated(
        message = "Use a flow instead.",
        replaceWith = ReplaceWith("getOrFetchWordDefinition(word: String)")
    )
    suspend fun fetchWordDefinition(word: String): Option<WordDefinition> {
        return withContext(defaultDispatcher) {
            return@withContext try {
                val response = dictionaryApi.fetchWordDefinition(word)
                if (response.isSuccessful) {
                    return@withContext Option.Success(response.body())
                }
                return@withContext Option.Error(
                    uiText = UiText.DynamicString(
                        response.message()
                            .ifBlank { "An error occurred fetching the word definition" })
                )
            } catch (e: Exception) {
                Option.Error(
                    message = e.message ?: "An error occurred fetching the word definition"
                )
            }
        }
    }
}