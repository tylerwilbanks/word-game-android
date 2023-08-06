package com.minutesock.wordgame

import androidx.compose.ui.graphics.Color

data class Letter(private val character: Char? = null, val color: Color = Color.Black) {
    val displayCharacter get() = character?.toString()?.uppercase() ?: ""
}