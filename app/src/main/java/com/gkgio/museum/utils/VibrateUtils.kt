package com.gkgio.museum.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class VibrateUtils private constructor(context: Context) {

    companion object {
        private const val DEFAULT_VIBRATE_DURATION = 30L

        private var INSTANCE: VibrateUtils? = null
        fun getInstance(context: Context): VibrateUtils {
            if (INSTANCE == null) {
                INSTANCE = VibrateUtils(context)
            }
            return INSTANCE!!
        }
    }

    private val vibrator: Vibrator by lazy {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun vibrateOneShot(duration: Long = DEFAULT_VIBRATE_DURATION) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    duration,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }

    fun vibratePattern(pattern: LongArray) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, -1)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(DEFAULT_VIBRATE_DURATION)
        }
    }
}