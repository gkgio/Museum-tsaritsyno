package com.gkgio.museum.feature.museum.detail

import android.os.Bundle
import android.view.View
import com.gkgio.museum.R
import com.gkgio.museum.base.BaseFragment
import com.gkgio.museum.di.AppInjector
import com.gkgio.museum.ext.createViewModel
import com.gkgio.museum.feature.model.Exhibition
import com.gkgio.museum.utils.FragmentArgumentDelegate
import kotlinx.android.synthetic.main.fragment_museum_detail_container.*

class MuseumDetailContainerFragment : BaseFragment<MuseumDetailContainerViewModel>() {

    companion object {
        fun newInstance(title: String, exhibitionsList: List<Exhibition>) =
            MuseumDetailContainerFragment().apply {
                this.exhibitionsList = exhibitionsList
                this.title = title
            }
    }

    private var museumDetailContainerPagerAdapter: MuseumDetailContainerPagerAdapter? = null

    private var exhibitionsList: List<Exhibition> by FragmentArgumentDelegate()
    private var title: String by FragmentArgumentDelegate()

    override fun getLayoutId(): Int = R.layout.fragment_museum_detail_container

    override fun provideViewModel() = createViewModel {
        AppInjector.appComponent.museumsDetailContainerViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setTitle(title)
        toolbar.setLeftIconClickListener {
            viewModel.onBackClick()
        }

        initViewPager()
    }

    private fun initViewPager() {
        museumDetailContainerPagerAdapter = MuseumDetailContainerPagerAdapter(
            exhibitionsList,
            requireContext(),
            childFragmentManager
        )
        exhibitionsViewPager.adapter = museumDetailContainerPagerAdapter
        exhibitionsTabs.setupWithViewPager(exhibitionsViewPager)
    }
}