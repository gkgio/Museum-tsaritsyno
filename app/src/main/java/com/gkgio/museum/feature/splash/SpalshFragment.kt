package com.gkgio.museum.feature.splash

import android.os.Bundle
import android.view.View
import com.gkgio.museum.R
import com.gkgio.museum.base.BaseFragment
import com.gkgio.museum.di.AppInjector
import com.gkgio.museum.ext.createViewModel


class SpalshFragment : BaseFragment<SplashViewModel>() {

    override fun getLayoutId(): Int = R.layout.fragment_splash

    override fun provideViewModel() = createViewModel {
        AppInjector.appComponent.splashViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}