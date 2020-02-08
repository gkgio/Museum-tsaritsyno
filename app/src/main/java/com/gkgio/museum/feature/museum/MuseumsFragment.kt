package com.gkgio.museum.feature.museum

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.gkgio.museum.R
import com.gkgio.museum.base.BaseFragment
import com.gkgio.museum.di.AppInjector
import com.gkgio.museum.ext.createViewModel
import com.gkgio.museum.ext.observeValue
import com.gkgio.museum.utils.DialogUtils
import kotlinx.android.synthetic.main.fragment_museums.*


class MuseumsFragment : BaseFragment<MuseumsViewModel>() {

    private var museumsAdapter: MuseumsAdapter? = null
    private var exhibitionsAdapter: ExhibitionsAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_museums

    override fun provideViewModel() = createViewModel {
        AppInjector.appComponent.museumsViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMuseumsRv()
        initExhibitionRv()

        viewModel.state.observeValue(this) { state ->
            state.museumsList?.let { museums ->
                museumsAdapter?.setMuseumList(museums)
                museumsTitle.isVisible = true
            }

            state.exhibitionsList?.let {
                exhibitionsAdapter?.setExhibitionsList(it)
                exhibitionsTitle.isVisible = true
            }

            swipeToRefresh.isRefreshing = state.isProgress
        }

        viewModel.errorEvent.observeValue(this) {
            DialogUtils.showError(view, it)
        }

        swipeToRefresh.setOnRefreshListener {
            viewModel.onSwipeToRefresh()
        }
    }

    private fun initMuseumsRv() {
        museumsAdapter = MuseumsAdapter {
            viewModel.onMuseumClick(it)
        }
        museumsList.adapter = museumsAdapter
        museumsList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initExhibitionRv() {
        exhibitionsAdapter = ExhibitionsAdapter {
            viewModel.onExhibitionClick(it)
        }
        exhibitionsList.adapter = exhibitionsAdapter
        exhibitionsList.layoutManager = LinearLayoutManager(context)
        exhibitionsList.isNestedScrollingEnabled = false
    }
}