package com.minutesock.core.uiutils

import android.content.Intent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun String.shareExternal(): Intent {
    val dataToShare = this
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, dataToShare)
        type = "text/plain"
    }
    return Intent.createChooser(sendIntent, null)
}

fun blendColors(color1: Color, color2: Color, ratio: Float) =
    Color(ColorUtils.blendARGB(color1.toArgb(), color2.toArgb(), ratio))
