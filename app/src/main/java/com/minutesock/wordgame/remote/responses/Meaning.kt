package com.minutesock.wordgame.remote.responses

data class Meaning(
    val definitions: List<Definition>,
    val partOfSpeech: String
)