package com.whx.jetpacktest.nav.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.whx.jetpacktest.nav.frag.NExploreFragment
import com.whx.jetpacktest.nav.frag.NFavoriteFragment
import com.whx.jetpacktest.nav.frag.NHomeFragment
import com.whx.jetpacktest.nav.frag.NPersonFragment
import java.security.InvalidParameterException

class FragAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    companion object {
        const val HOME = 0
        const val EXPLORE = 1
        const val FAVORITE = 2
        const val PERSON = 3
    }
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            HOME -> NHomeFragment()
            EXPLORE -> NExploreFragment()
            FAVORITE -> NFavoriteFragment()
            PERSON -> NPersonFragment()
            else -> throw InvalidParameterException("$position is invalid")
        }
    }
}