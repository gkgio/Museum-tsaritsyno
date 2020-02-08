package com.gkgio.museum.feature.audios

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.gkgio.museum.R
import com.gkgio.museum.base.bottomsheet.BaseBottomSheetDialog
import com.gkgio.museum.ext.withCenterCropRoundedCorners
import com.gkgio.museum.ext.withFade
import com.gkgio.museum.feature.model.Item
import com.gkgio.museum.utils.FragmentArgumentDelegate
import kotlinx.android.synthetic.main.layout_audio_player.view.*
import org.altbeacon.beacon.Beacon

class AudioPlayerSheet : BaseBottomSheetDialog() {

    companion object {
        const val KEY_BEACON = "KEY_BEACON"
        const val KEY_ITEM = "KEY_ITEM"

        fun newInstance(beacon: Beacon? = null, item: Item? = null) = AudioPlayerSheet().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_BEACON, beacon)
                putParcelable(KEY_ITEM, item)
            }
        }
    }

    private var beacon: Beacon? = null
    private var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            beacon = it.getParcelable(KEY_BEACON)
            item = it.getParcelable(KEY_ITEM)
        }
    }

    override fun getLayoutId(): Int = R.layout.layout_audio_player

    override fun setupView(view: View): Unit = with(view) {
        super.setupView(view)

        //var url = "https://www.kozco.com/tech/piano2-CoolEdit.mp3"
        item?.let {
            Glide.with(context)
                .load(it.images[0])
                .withFade()
                .withCenterCropRoundedCorners(context)
                .into(imageItem)

            description.text = it.description

            val mediaPlayer: MediaPlayer? = MediaPlayer().apply {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDataSource(it.audioFiles[0])
                prepare() // might take long! (for buffering, etc)
                start()
            }

            mediaPlayer?.setOnCompletionListener { player ->
                player.release()
            }
        }
    }
}