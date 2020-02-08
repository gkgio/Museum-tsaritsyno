package com.gkgio.museum.feature.audios

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import androidx.core.net.toUri
import com.gkgio.museum.R
import com.gkgio.museum.base.bottomsheet.BaseBottomSheetDialog
import com.gkgio.museum.utils.FragmentArgumentDelegate
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

        val url = "https://www.kozco.com/tech/piano2-CoolEdit.mp3"
        val mediaPlayer: MediaPlayer? = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDataSource(url)
            prepare() // might take long! (for buffering, etc)
            start()
        }
    }
}