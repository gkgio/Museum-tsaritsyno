package com.gkgio.museum.activity

import android.content.SharedPreferences
import com.gkgio.museum.base.BaseViewModel
import com.gkgio.museum.navigation.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class LaunchViewModel @Inject constructor(
    private val router: Router,
    private val prefs: SharedPreferences
) : BaseViewModel() {

    companion object {
        const val START_PAGE_KEY = "start_page_key"
    }

    fun openSplashFragment() {
        val startWasShown = prefs.getBoolean(START_PAGE_KEY, false)
        if (startWasShown) {
            router.newRootScreen(Screens.MainFragmentScreen)
        } else {
            router.newRootScreen(Screens.StartFragmentScreen)
        }
    }
}