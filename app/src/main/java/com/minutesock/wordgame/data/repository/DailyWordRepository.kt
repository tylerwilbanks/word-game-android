package com.minutesock.wordgame.data.repository

import com.minutesock.wordgame.data.WordInfoDao
import com.minutesock.wordgame.domain.WordInfo
import com.minutesock.wordgame.mappers.toWordInfo
import com.minutesock.wordgame.mappers.toWordInfoEntity
import com.minutesock.wordgame.remote.DictionaryApi
import com.minutesock.wordgame.remote.RetrofitInstance
import com.minutesock.wordgame.uiutils.UiText
import com.minutesock.wordgame.utils.Option
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class DailyWordRepository(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dictionaryApi: DictionaryApi = RetrofitInstance.dictionaryApi,
    private val wordInfoDao: WordInfoDao
) {

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
}