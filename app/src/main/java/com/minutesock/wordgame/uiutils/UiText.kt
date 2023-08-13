package com.minutesock.wordgame.uiutils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class DynamicString(
        val value: String
    ) : UiText()

    data class StringResource(
        @StringRes val id: Int,
        val args: List<Any>? = null,
    ) : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> {
                if (args == null) {
                    context.getString(id)
                } else {
                    context.getString(id, *args.toTypedArray())
                }
            }
        }
    }

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource ->
                if (args == null) {
                    stringResource(id)
                } else {
                    stringResource(
                        id,
                        *args.toTypedArray()
                    )
                }
        }
    }
}
