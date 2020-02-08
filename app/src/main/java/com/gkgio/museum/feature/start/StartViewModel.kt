package com.gkgio.museum.feature.start

import android.content.SharedPreferences
import com.gkgio.horoscope.utils.SingleLiveEvent
import com.gkgio.museum.activity.LaunchViewModel
import com.gkgio.museum.base.BaseViewModel
import com.gkgio.museum.navigation.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class StartViewModel @Inject constructor(
    private val router: Router,
    prefs: SharedPreferences
) : BaseViewModel() {
    val showWelcomeBanner = SingleLiveEvent<Unit>()

    init {
        prefs.edit().putBoolean(LaunchViewModel.START_PAGE_KEY, true).apply()
    }

    fun onContinueBtnCLick() {
        showWelcomeBanner.call()
    }

    fun onConfirmWelcomeClick() {
        router.newRootScreen(Screens.MainFragmentScreen)
    }
}