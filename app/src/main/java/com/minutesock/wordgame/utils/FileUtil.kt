package com.minutesock.wordgame.utils

import android.content.Context
import org.json.JSONArray
import java.io.IOException

class FileUtil {
    fun readAssetFile(context: Context, filename: String): String? {
        val assetManager = context.assets
        return try {
            val inputStream = assetManager.open(filename)
            inputStream.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun obtainJsonArray(context: Context, filename: String): JSONArray? {
        val fileText = readAssetFile(context, filename)
        return try {
            JSONArray(fileText)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}