package com.gkgio.museum.feature.auth

import android.content.SharedPreferences
import com.gkgio.museum.activity.LaunchViewModel
import com.gkgio.museum.base.BaseViewModel
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val prefs: SharedPreferences,
    private val router: Router
) : BaseViewModel() {

    companion object {
        const val WELCOME_BANNER_KEY = "welcome_banner_key"
    }

    init {
        val isWelcomeBannerWasShown = prefs.getBoolean(WELCOME_BANNER_KEY, false)
        
    }
}