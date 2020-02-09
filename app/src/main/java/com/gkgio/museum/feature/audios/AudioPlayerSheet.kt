package com.gkgio.museum.feature.audios

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.gkgio.museum.R
import com.gkgio.museum.base.bottomsheet.BaseBottomSheetDialog
import com.gkgio.museum.di.AppInjector
import com.gkgio.museum.ext.*
import com.gkgio.museum.feature.model.Item
import kotlinx.android.synthetic.main.layout_audio_player.*
import kotlinx.android.synthetic.main.layout_audio_player.view.*

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
    //private var seekbarUpdateHandler: Handler? = null
    //private var updateSeekbar: Runnable? = null

    private val seekBarUpdateHandler: Handler = Handler()
    private val updateSeekBar: Runnable = object : Runnable {
        override fun run() {
            mediaPlayer?.let { player ->
                timeSeekBar.progress = player.currentPosition
                seekBarUpdateHandler.postDelayed(this, 100)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel { AppInjector.appComponent.audioPlayerViewModel }

        viewModel.closeSheet.observeValue(this) {
            dismiss()
        }

        viewModel.playOrPause.observeValue(this) { isPlay ->
            mediaPlayer?.let { player ->
                if (isPlay) {
                    seekBarUpdateHandler.postDelayed(updateSeekBar, 0)
                    player.seekTo(player.currentPosition)
                    player.start()
                    playPauseIcon.setImageResource(R.drawable.ic_pause)
                } else {
                    seekBarUpdateHandler.removeCallbacks(updateSeekBar)
                    player.pause()
                    playPauseIcon.setImageResource(R.drawable.ic_play)
                }
            }
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

            mediaPlayer?.let { player ->
                player.setOnCompletionListener {
                    player.release()
                }

                player.setOnPreparedListener {
                    seekBarUpdateHandler.post(updateSeekBar)
                }

                timeSeekBar.max = player.duration

                timeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    }

                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        if (fromUser) {
                            player.seekTo(progress)
                        }
                    }

                })
            }

            playPauseIcon.setDebounceOnClickListener {
                viewModel.onPlayOrPauseButtonClick()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.reset()
        seekBarUpdateHandler.removeCallbacks(updateSeekBar)

    }
}