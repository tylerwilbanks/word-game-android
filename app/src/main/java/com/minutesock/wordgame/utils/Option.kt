package com.minutesock.wordgame.utils

import com.minutesock.wordgame.uiutils.UiText

sealed class Option<T>(
    val data: T? = null,
    val message: String? = null,
    val uiText: UiText? = null,
    val errorCode: Int? = null
) {

    class Loading<T>(data: T? = null) : Option<T>(data = data)

    class Success<T>(data: T?) : Option<T>(data = data)

    class Error<T>(
        uiText: UiText? = null,
        message: String? = null,
        data: T? = null,
        errorCode: Int? = null
    ) :
        Option<T>(
            data = data,
            uiText = uiText,
            message = message,
            errorCode = errorCode
        )

    val hasMessageToDisplay get() = uiText != null
}