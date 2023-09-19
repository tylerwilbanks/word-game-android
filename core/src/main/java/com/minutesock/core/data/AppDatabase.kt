package com.minutesock.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.minutesock.core.data.models.WordInfoEntity
import com.minutesock.core.data.models.WordSessionEntity
import com.minutesock.core.utils.GsonParser

@Database(
    entities = [WordSessionEntity::class, WordInfoEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun WordSessionDao(): WordSessionDao
    abstract fun WordInfoDao(): WordInfoDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val inst = instance
            if (inst != null) {
                return inst
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "word_app_database"
                )
                    .addTypeConverter(Converters(GsonParser(Gson())))
                    .build()
                Companion.instance = instance
                return instance
            }
        }
    }
}