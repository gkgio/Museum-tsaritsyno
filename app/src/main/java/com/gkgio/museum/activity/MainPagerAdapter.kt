package com.gkgio.museum.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.gkgio.museum.feature.profile.ProfileFragment
import com.gkgio.museum.feature.audios.AudiosFragment
import com.gkgio.museum.feature.museum.MuseumsFragment

class MainPagerAdapter(
    fm: FragmentManager
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        const val PAGE_COUNT = 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MuseumsFragment()
            1 -> AudiosFragment()
            2 -> ProfileFragment()
            else -> throw IllegalArgumentException("Unsupported tab")
        }
    }

    override fun getCount(): Int = PAGE_COUNT
}
