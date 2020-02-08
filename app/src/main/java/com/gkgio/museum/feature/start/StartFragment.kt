package com.gkgio.museum.feature.start

import android.os.Bundle
import android.view.View
import com.gkgio.museum.R
import com.gkgio.museum.base.BaseFragment
import com.gkgio.museum.di.AppInjector
import com.gkgio.museum.ext.createViewModel
import com.gkgio.museum.ext.observeValue
import com.gkgio.museum.ext.setDebounceOnClickListener
import kotlinx.android.synthetic.main.fragment_start.*
import kotlinx.android.synthetic.main.view_select_language.view.*

class StartFragment : BaseFragment<StartViewModel>(),
    WelcomeBannerSheet.WelcomeBannerClickListener {

    companion object {
        val TAG = StartFragment::class.java.simpleName
    }

    override fun getLayoutId(): Int = R.layout.fragment_start

    override fun provideViewModel() = createViewModel {
        AppInjector.appComponent.startViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        interfaceLanguage.title.text = getString(R.string.select_interface_language)
        audioLanguage.title.text = getString(R.string.select_audio_language)

        continueBtn.setDebounceOnClickListener {
            viewModel.onContinueBtnCLick()
        }

        viewModel.showWelcomeBanner.observeValue(this) {
            showDialog(WelcomeBannerSheet(), TAG)
        }
    }

    override fun selectedWelcomeBannerConfirm() {
        viewModel.onConfirmWelcomeClick()
    }
}