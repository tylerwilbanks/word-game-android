package com.minutesock.core

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.minutesock.core.data.AppDatabase


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Companion.applicationContext = applicationContext
    }

    companion object {

        val database by lazy { AppDatabase.getDatabase(applicationContext) }

        val isDarkMode by lazy {
            when (applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> true
                Configuration.UI_MODE_NIGHT_NO -> false
                else -> false
            }
        }

        lateinit var applicationContext: Context
            private set
    }
}