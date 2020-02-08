package com.gkgio.museum.feature.museum.detail

import androidx.lifecycle.MutableLiveData
import com.gkgio.horoscope.utils.SingleLiveEvent
import com.gkgio.museum.base.BaseViewModel
import com.gkgio.museum.ext.isNonInitialized
import com.gkgio.museum.feature.model.Exhibition
import com.gkgio.museum.feature.model.Item
import com.gkgio.museum.feature.model.Museum
import com.gkgio.museum.feature.museum.MuseumsViewModel
import com.gkgio.museum.navigation.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MuseumDetailViewModel @Inject constructor(
    private val router: Router
) : BaseViewModel() {

    val state = MutableLiveData<State>()

    fun init(type: String, exhibitionsList: List<Exhibition>) {
        if (state.isNonInitialized()) {
            val typeFilterExhibitionsList = mutableListOf<Exhibition>()
            exhibitionsList.forEach {
                if (it.type == type) {
                    typeFilterExhibitionsList.add(it)
                }
            }
            state.value = State(typeFilterExhibitionsList)
        }
    }

    fun onExhibitionClick(exhibition: Exhibition) {
        exhibition.items?.let {
            router.navigateTo(Screens.ItemsFramentScreen(exhibition.title, it))
        }
    }

    data class State(
        val exhibitionsList: MutableList<Exhibition>? = null
    )
}