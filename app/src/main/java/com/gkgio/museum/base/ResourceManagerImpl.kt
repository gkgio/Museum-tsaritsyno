package com.gkgio.museum.base

import android.content.Context
import java.io.IOException
import javax.inject.Inject

class ResourceManagerImpl @Inject constructor(
    private val context: Context
) : ResourceManager {

    override fun getJsonFromAssets(fileName: String): String? = try {
        val inputStream = context.assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, charset("UTF-8"))
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

