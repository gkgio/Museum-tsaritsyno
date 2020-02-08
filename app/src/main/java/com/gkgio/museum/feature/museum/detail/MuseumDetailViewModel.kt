package com.gkgio.museum.feature.museum.detail

import androidx.lifecycle.MutableLiveData
import com.gkgio.horoscope.utils.SingleLiveEvent
import com.gkgio.museum.base.BaseViewModel
import com.gkgio.museum.ext.isNonInitialized
import com.gkgio.museum.feature.model.Exhibition
import com.gkgio.museum.feature.model.Museum
import com.gkgio.museum.feature.museum.MuseumsViewModel
import javax.inject.Inject

class MuseumDetailViewModel @Inject constructor(
) : BaseViewModel() {

    val openItem = SingleLiveEvent<Exhibition>()
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
        openItem.value = exhibition
    }

    data class State(
        val exhibitionsList: MutableList<Exhibition>? = null
    )
}