package com.gkgio.museum.feature.museum

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gkgio.museum.R
import com.gkgio.museum.base.BaseFragment
import com.gkgio.museum.di.AppInjector
import com.gkgio.museum.ext.createViewModel
import com.gkgio.museum.ext.requestLocationPermission
import kotlinx.android.synthetic.main.fragment_museums.*


class MuseumsFragment : BaseFragment<MuseumsViewModel>() {

    private var museumsAdapter: MuseumsAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_museums

    override fun provideViewModel() = createViewModel {
        AppInjector.appComponent.museumsViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMuseumsRv()
    }

    private fun initMuseumsRv() {
        museumsAdapter = MuseumsAdapter {
            viewModel.onMuseumClick(it)
        }
        museumsList.adapter = museumsAdapter
        museumsList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
}