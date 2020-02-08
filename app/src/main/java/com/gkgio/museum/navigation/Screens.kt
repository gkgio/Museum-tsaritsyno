package com.gkgio.museum.navigation

import android.content.Context
import com.gkgio.museum.utils.IntentUtils
import com.gkgio.museum.activity.MainFragment
import com.gkgio.museum.feature.auth.AuthFragment
import com.gkgio.museum.feature.museum.MuseumsFragment
import com.gkgio.museum.feature.profile.ProfileFragment
import com.gkgio.museum.feature.splash.SpalshFragment
import com.gkgio.museum.feature.start.StartFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object SettingsFragmentScreen : SupportAppScreen() {
        override fun getFragment() = ProfileFragment()
    }

    object MainFragmentScreen : SupportAppScreen() {
        override fun getFragment() = MainFragment()
    }

    object SpalshFragmentScreen : SupportAppScreen() {
        override fun getFragment() = SpalshFragment()
    }

    object StartFragmentScreen : SupportAppScreen() {
        override fun getFragment() = StartFragment()
    }

    object MuseumsFragmentScreen : SupportAppScreen() {
        override fun getFragment() = MuseumsFragment()
    }

    object AuthFragmentScreen : SupportAppScreen() {
        override fun getFragment() = AuthFragment()
    }

    class EmailScreen(
        private val email: String
    ) : SupportAppScreen() {
        override fun getActivityIntent(context: Context?) =
            IntentUtils.createEmailIntent(email)
    }
}