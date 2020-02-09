package com.gkgio.museum.feature.audios

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.gkgio.museum.R
import com.gkgio.museum.base.bottomsheet.BaseBottomSheetDialog
import com.gkgio.museum.di.AppInjector
import com.gkgio.museum.ext.createViewModel
import com.gkgio.museum.ext.observeValue
import com.gkgio.museum.ext.withCenterCropRoundedCorners
import com.gkgio.museum.ext.withFade
import com.gkgio.museum.feature.model.Item
import com.gkgio.museum.utils.FragmentArgumentDelegate
import kotlinx.android.synthetic.main.layout_audio_player.view.*
import org.altbeacon.beacon.Beacon

class AudioPlayerSheet : BaseBottomSheetDialog() {

    companion object {
        const val KEY_ITEM = "KEY_ITEM"

        fun newInstance(item: Item) = AudioPlayerSheet().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_ITEM, item)
            }
        }
    }

    private var item: Item? = null
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var viewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel { AppInjector.appComponent.audioPlayerViewModel }

        viewModel.closeSheet.observeValue(this) {
            dismiss()
        }

        arguments?.let {
            item = it.getParcelable(KEY_ITEM)
        }
    }

    override fun getLayoutId(): Int = R.layout.layout_audio_player

    override fun setupView(view: View): Unit = with(view) {
        super.setupView(view)

        item?.let {

            title.text = it.title
            title.isVisible = !it.title.isNullOrBlank()

            Glide.with(context)
                .load(it.images[0])
                .withFade()
                .withCenterCropRoundedCorners(context)
                .into(imageItem)

            description.text = it.description

            mediaPlayer = MediaPlayer().apply {
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

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.reset()
    }
}