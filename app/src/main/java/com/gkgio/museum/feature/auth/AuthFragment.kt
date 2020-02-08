package com.gkgio.museum.feature.auth

import android.os.Bundle
import android.view.View
import com.gkgio.museum.R
import com.gkgio.museum.base.BaseFragment
import com.gkgio.museum.di.AppInjector
import com.gkgio.museum.ext.createViewModel
import com.gkgio.museum.feature.museum.MuseumsViewModel

class AuthFragment : BaseFragment<AuthViewModel>() {

    override fun getLayoutId(): Int = R.layout.fragment_auth

    override fun provideViewModel() = createViewModel {
        AppInjector.appComponent.authViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}