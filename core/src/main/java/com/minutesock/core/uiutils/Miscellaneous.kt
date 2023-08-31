package com.minutesock.core.uiutils

import android.content.Intent

fun String.shareExternal(): Intent {
    val dataToShare = this
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, dataToShare)
        type = "text/plain"
    }
    return Intent.createChooser(sendIntent, null)
}