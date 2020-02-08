package com.gkgio.museum.feature.museum.detail

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gkgio.museum.R
import com.gkgio.museum.base.BaseFragment
import com.gkgio.museum.di.AppInjector
import com.gkgio.museum.ext.createViewModel
import com.gkgio.museum.ext.observeValue
import com.gkgio.museum.feature.audios.AudioPlayerSheet
import com.gkgio.museum.feature.model.Exhibition
import com.gkgio.museum.feature.museum.ExhibitionsAdapter
import com.gkgio.museum.feature.profile.ProfileViewModel
import com.gkgio.museum.feature.start.WelcomeBannerSheet
import com.gkgio.museum.navigation.Screens
import com.gkgio.museum.utils.FragmentArgumentDelegate
import kotlinx.android.synthetic.main.fragment_museum_detail.*

class MuseumDetailFragment : BaseFragment<MuseumDetailViewModel>() {

    companion object {
        val TAG = MuseumDetailFragment::class.java.simpleName

        fun newInstance(type: String, exhibitionsList: List<Exhibition>) =
            MuseumDetailFragment().apply {
                this.type = type
                this.exhibitionsList = exhibitionsList
            }
    }

    private var exhibitionsAdapter: ExhibitionsAdapter? = null

    private var type: String by FragmentArgumentDelegate()
    private var exhibitionsList: List<Exhibition> by FragmentArgumentDelegate()

    override fun getLayoutId(): Int = R.layout.fragment_museum_detail

    override fun provideViewModel() = createViewModel {
        AppInjector.appComponent.museumsDetailViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initExhibitionRv()

        viewModel.init(type, exhibitionsList)

        viewModel.state.observeValue(this) { state ->
            state.exhibitionsList?.let {
                exhibitionsAdapter?.setExhibitionsList(it)
            }
        }
    }

    private fun initExhibitionRv() {
        exhibitionsAdapter = ExhibitionsAdapter {
            viewModel.onExhibitionClick(it)
        }
        exhibitionsRecycler.adapter = exhibitionsAdapter
        exhibitionsRecycler.layoutManager = LinearLayoutManager(context)
    }
}