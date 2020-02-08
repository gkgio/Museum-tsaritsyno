package com.gkgio.museum.feature.start

import android.content.Context
import android.view.View
import com.gkgio.museum.R
import com.gkgio.museum.base.bottomsheet.BaseBottomSheetDialog
import com.gkgio.museum.ext.setDebounceOnClickListener
import kotlinx.android.synthetic.main.layout_welcome_banner.view.*
import kotlinx.android.synthetic.main.view_welcome_item.view.*

class WelcomeBannerSheet : BaseBottomSheetDialog() {

    private lateinit var clickListener: WelcomeBannerClickListener

    override fun getLayoutId(): Int = R.layout.layout_welcome_banner

    override fun onAttach(context: Context) {
        super.onAttach(context)
        clickListener = when {
            parentFragment is WelcomeBannerClickListener -> parentFragment as WelcomeBannerClickListener
            context is WelcomeBannerClickListener -> context
            else -> throw IllegalStateException("Either parentFragment or context must implement ItemClickListener")
        }
    }

    override fun setupView(view: View) = with(view) {
        super.setupView(view)

        audioWelcomeItem.imageWelcome.setImageResource(R.drawable.ic_audios)
        audioWelcomeItem.textWelcomeItem.text = getString(R.string.welcome_listen_text)

        autoWelcomeItem.imageWelcome.setImageResource(R.drawable.ic_active_receiver)
        autoWelcomeItem.textWelcomeItem.text = getString(R.string.welcome_auto_work_text)

        readWelcomeItem.imageWelcome.setImageResource(R.drawable.ic_read)
        readWelcomeItem.textWelcomeItem.text = getString(R.string.welcome_read_inform_text)

        confirmBtn.setDebounceOnClickListener {
            clickListener.selectedWelcomeBannerConfirm()
        }
    }

    interface WelcomeBannerClickListener {
        fun selectedWelcomeBannerConfirm()
    }
}