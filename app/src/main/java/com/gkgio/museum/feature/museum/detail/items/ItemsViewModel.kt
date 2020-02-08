package com.gkgio.museum.feature.museum.detail.items

import androidx.lifecycle.MutableLiveData
import com.gkgio.horoscope.utils.SingleLiveEvent
import com.gkgio.museum.base.BaseViewModel
import com.gkgio.museum.feature.model.Exhibition
import com.gkgio.museum.feature.model.Item
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class ItemsViewModel @Inject constructor(
    private val router: Router
) : BaseViewModel() {

    val openItemSheet = SingleLiveEvent<Item>()
    val state = MutableLiveData<State>()

    fun init(itemsList: MutableList<Item>) {
        state.value = State(itemsList)
    }

    fun onItemClick(item: Item) {
        openItemSheet.value = item
    }

    fun onBackClick() {
        router.exit()
    }

    data class State(
        val itemsList: MutableList<Item>? = null
    )
}