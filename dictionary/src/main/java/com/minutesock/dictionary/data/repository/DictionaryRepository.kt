package com.minutesock.dictionary.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DictionaryRepository(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

}