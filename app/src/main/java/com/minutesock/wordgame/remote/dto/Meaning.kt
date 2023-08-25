package com.minutesock.wordgame.remote.dto

data class Meaning(
    val definitions: List<Definition>,
    val partOfSpeech: String
)