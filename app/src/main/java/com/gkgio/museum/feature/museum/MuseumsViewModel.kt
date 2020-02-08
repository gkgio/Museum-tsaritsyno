package com.gkgio.museum.feature.museum

import android.content.SharedPreferences
import com.gkgio.museum.base.BaseViewModel
import com.gkgio.museum.feature.model.Museum
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MuseumsViewModel @Inject constructor(
    private val prefs: SharedPreferences,
    private val router: Router
) : BaseViewModel() {

    init {

    }

    fun onMuseumClick(museum: Museum) {

    }

}