package com.gkgio.museum.feature.museum.detail

import com.gkgio.museum.base.BaseViewModel
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MuseumDetailContainerViewModel @Inject constructor(
    private val router: Router
) : BaseViewModel() {

    fun onBackClick() {
        router.exit()
    }
}
