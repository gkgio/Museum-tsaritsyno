package com.gkgio.museum.feature.museum.detail

import android.os.Bundle
import android.view.View
import com.gkgio.museum.R
import com.gkgio.museum.base.BaseFragment
import com.gkgio.museum.di.AppInjector
import com.gkgio.museum.ext.createViewModel
import com.gkgio.museum.feature.profile.ProfileViewModel

class MuseumDetailFragment : BaseFragment<ProfileViewModel>() {

    override fun getLayoutId(): Int = R.layout.fragment_profile

    override fun provideViewModel() = createViewModel {
        AppInjector.appComponent.profileViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}