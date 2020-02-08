package com.gkgio.museum.feature.profile

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.gkgio.museum.base.BaseViewModel
import com.gkgio.museum.ext.isNonInitialized
import com.google.firebase.analytics.FirebaseAnalytics
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val prefs: SharedPreferences,
    private val router: Router,
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseViewModel() {

    val state = MutableLiveData<State>()

    init {
        if (state.isNonInitialized()) {

        }
    }



    data class State(
        val isNightModeOn: Boolean = true
    )
}