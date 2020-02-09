package com.gkgio.museum.feature.audios

import com.gkgio.horoscope.utils.SingleLiveEvent
import com.gkgio.museum.base.BaseViewModel
import com.gkgio.museum.ext.applySchedulers
import com.gkgio.museum.feature.model.Item
import com.gkgio.museum.utils.AudioDissmisEvent
import javax.inject.Inject

class AudioPlayerViewModel @Inject constructor(
    private val audioDissmisEvent: AudioDissmisEvent
) : BaseViewModel() {

    val closeSheet = SingleLiveEvent<Unit>()

    init {
        audioDissmisEvent
            .getEventResult()
            .applySchedulers()
            .subscribe {
                closeSheet.call()
            }.addDisposable()
    }
}