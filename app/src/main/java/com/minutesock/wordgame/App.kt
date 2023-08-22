package com.minutesock.wordgame

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
        fun get(): App {
            return instance
        }
    }
}