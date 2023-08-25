package com.minutesock.wordgame

import android.app.Application
import android.content.Context
import com.minutesock.wordgame.data.AppDatabase


class App : Application() {


    override fun onCreate() {
        super.onCreate()
        App.applicationContext = applicationContext
    }

    companion object {

        val database by lazy { AppDatabase.getDatabase(applicationContext) }

        lateinit var applicationContext: Context
            private set
    }
}

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        var appContext: Context? = null
            private set
    }
}