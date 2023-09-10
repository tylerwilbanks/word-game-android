package com.minutesock.daily.data.repository

import android.util.Log
import com.minutesock.core.data.DailyWordSessionDao
import com.minutesock.core.data.WordInfoDao
import com.minutesock.core.domain.DailyWordSession
import com.minutesock.core.domain.WordInfo
import com.minutesock.core.mappers.DATE_FORMAT_PATTERN
import com.minutesock.core.mappers.toDailyWordSession
import com.minutesock.core.mappers.toDailyWordSessionEntity
import com.minutesock.core.mappers.toWordInfo
import com.minutesock.core.mappers.toWordInfoEntity
import com.minutesock.core.remote.DictionaryApi
import com.minutesock.core.remote.RetrofitInstance
import com.minutesock.core.uiutils.UiText
import com.minutesock.core.utils.Option
import com.minutesock.core.utils.asDaysToMillis
import com.minutesock.core.utils.toString
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.Date

class DailyWordRepository(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dictionaryApi: DictionaryApi = RetrofitInstance.dictionaryApi,
    private val wordInfoDao: WordInfoDao,
    private val dailyWordSessionDao: DailyWordSessionDao
) {

    suspend fun getOrFetchWordDefinition(word: String): Flow<Option<List<WordInfo>>> = flow {
        emit(Option.Loading())

        val data = wordInfoDao.getWordInfos(word).map { it.toWordInfo() }
        emit(Option.Loading(data = data))

        val throttleExpireDate = data.firstOrNull()?.fetchDate?.time?.plus(14L.asDaysToMillis())

        if (throttleExpireDate != null && Date().before(Date(throttleExpireDate))) {
            Log.e(null, "Fetch definition throttled!")
            return@flow
        }

        try {
            Log.e(null, "Fetching definition!")
            val response = dictionaryApi.fetchWordDefinition(word)
            if (response.isSuccessful) {
                response.body()?.let { remoteWordDefinitions ->
                    wordInfoDao.deleteWordInfos(remoteWordDefinitions.map { it.word })
                    wordInfoDao.insertWordInfos(remoteWordDefinitions.map { it.toWordInfoEntity(Date()) })
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

    suspend fun saveDailySession(dailyWordSession: DailyWordSession) {
        withContext(defaultDispatcher) {
            dailyWordSessionDao.insert(dailyWordSession.toDailyWordSessionEntity())
        }
    }

    suspend fun loadDailySession(todayDate: Date): DailyWordSession? {
        return withContext(defaultDispatcher) {
            dailyWordSessionDao.getTodaySession(todayDate.toString(DATE_FORMAT_PATTERN))
                ?.toDailyWordSession()
        }
    }

    suspend fun deleteDailySession(todayDate: Date) {
        withContext(defaultDispatcher) {
            loadDailySession(todayDate)?.let {
                dailyWordSessionDao.delete(it.toDailyWordSessionEntity())
            }
        }
    }

}