package com.gkgio.museum.feature.museum.detail

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gkgio.museum.R
import com.gkgio.museum.feature.model.Exhibition

class MuseumDetailContainerPagerAdapter(
    private val exhibitionsList: List<Exhibition>,
    private val context: Context,
    fm: FragmentManager
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private companion object {
        private const val TABS_COUNT = 2
    }

    override fun getItem(position: Int) = when (position) {
        0 -> MuseumDetailFragment.newInstance("permanent", exhibitionsList)
        else -> MuseumDetailFragment.newInstance("temporary", exhibitionsList)
    }

    override fun getPageTitle(position: Int) = when (position) {
        0 -> context.getString(R.string.permanent_exhibition)
        else -> context.getString(R.string.temporary_exhibition)
    }

    override fun getCount() = TABS_COUNT
}