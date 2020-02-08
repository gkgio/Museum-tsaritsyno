package com.gkgio.museum.base

interface ResourceManager {
    fun getJsonFromAssets(fileName: String): String?
}