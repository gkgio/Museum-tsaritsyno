package com.gkgio.museum.feature.audio

import android.content.Context
import android.view.View
import com.gkgio.museum.R
import com.gkgio.museum.base.bottomsheet.BaseBottomSheetDialog
import com.gkgio.museum.ext.setDebounceOnClickListener
import com.gkgio.museum.utils.FragmentArgumentDelegate
import kotlinx.android.synthetic.main.layout_welcome_banner.view.*
import kotlinx.android.synthetic.main.view_welcome_item.view.*
import org.altbeacon.beacon.Beacon

class AudioPlayerSheet : BaseBottomSheetDialog() {

    companion object {
        fun newInstance(beacon: Beacon) = AudioPlayerSheet().apply {
            this.beacon = beacon
        }
    }

    private var beacon: Beacon by FragmentArgumentDelegate()

    override fun getLayoutId(): Int = R.layout.layout_audio_player

    override fun setupView(view: View) = with(view) {
        super.setupView(view)

    }
}