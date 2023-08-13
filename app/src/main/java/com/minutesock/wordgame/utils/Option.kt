package com.minutesock.wordgame.utils

import com.minutesock.wordgame.uiutils.UiText

sealed class Option<T>(
    val data: T? = null,
    val message: String? = null,
    val uiText: UiText? = null,
    val errorCode: Int? = null
) {
    class Success<T>(data: T?) : Option<T>(data = data)

    class UiError<T>(uiText: UiText, data: T? = null, errorCode: Int? = null) :
        Option<T>(
            data = data,
            uiText = uiText,
            errorCode = errorCode
        )

    class Error<T>(message: String, data: T? = null, errorCode: Int? = null) :
        Option<T>(
            data = data,
            message = message,
            errorCode = errorCode
        )
}