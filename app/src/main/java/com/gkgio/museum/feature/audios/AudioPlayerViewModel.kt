package com.gkgio.museum.feature.audios

import com.gkgio.horoscope.utils.SingleLiveEvent
import com.gkgio.museum.base.BaseViewModel
import com.gkgio.museum.ext.applySchedulers
import com.gkgio.museum.utils.AudioDissmisEvent
import javax.inject.Inject

class AudioPlayerViewModel @Inject constructor(
    private val audioDismissEvent: AudioDissmisEvent
) : BaseViewModel() {

    var isPlay = true

    val closeSheet = SingleLiveEvent<Unit>()
    val playOrPause = SingleLiveEvent<Boolean>()

    init {
        audioDismissEvent
            .getEventResult()
            .applySchedulers()
            .subscribe {
                closeSheet.call()
            }.addDisposable()
    }

    fun onPlayOrPauseButtonClick() {
        isPlay = !isPlay
        playOrPause.value = isPlay
    }
}