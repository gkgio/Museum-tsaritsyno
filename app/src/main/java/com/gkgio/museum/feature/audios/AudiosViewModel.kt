package com.gkgio.museum.feature.audios

import android.content.SharedPreferences
import com.gkgio.museum.base.BaseViewModel
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class AudiosViewModel @Inject constructor(
    private val prefs: SharedPreferences,
    private val router: Router
) : BaseViewModel() {
}