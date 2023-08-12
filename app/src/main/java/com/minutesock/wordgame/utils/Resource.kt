package com.minutesock.wordgame.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null, errorCode: Int? = null) :
        Resource<T>(data, message)
}