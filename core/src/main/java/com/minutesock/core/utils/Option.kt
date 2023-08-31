package com.minutesock.core.utils

sealed class Option<T>(
    val data: T? = null,
    val message: String? = null,
    val uiText: com.minutesock.core.uiutils.UiText? = null,
    val errorCode: Int? = null
) {

    class Loading<T>(data: T? = null) : Option<T>(data = data)

    class Success<T>(data: T?) : Option<T>(data = data)

    class Error<T>(
        uiText: com.minutesock.core.uiutils.UiText? = null,
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