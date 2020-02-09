package com.gkgio.museum.feature.audios

import android.os.Bundle
import android.view.View
import com.gkgio.museum.R
import com.gkgio.museum.base.BaseFragment
import com.gkgio.museum.di.AppInjector
import com.gkgio.museum.ext.createViewModel


class AudiosFragment : BaseFragment<AudiosViewModel>() {

    override fun getLayoutId(): Int = R.layout.fragment_audios

    override fun provideViewModel() = createViewModel {
        AppInjector.appComponent.audiosViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}