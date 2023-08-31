package com.minutesock.core

import android.app.Application
import android.content.Context
import com.minutesock.core.data.AppDatabase


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Companion.applicationContext = applicationContext
    }

    companion object {

        val database by lazy { AppDatabase.getDatabase(applicationContext) }

        lateinit var applicationContext: Context
            private set
    }
}