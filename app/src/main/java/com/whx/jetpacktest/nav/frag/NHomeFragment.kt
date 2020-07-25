package com.whx.jetpacktest.nav.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navOptions
import com.whx.jetpacktest.BaseFragment
import com.whx.jetpacktest.R
import com.whx.jetpacktest.nav.NavManager
import kotlinx.android.synthetic.main.fragment_nav_home.*

class NHomeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nav_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        to_fragA.setOnClickListener {
            NavManager.navigate(NFragmentA::class)
        }
        to_fragB.setOnClickListener {
            val options = navOptions {
                anim {
                    enter = R.anim.anim_bottom_in
                    exit = R.anim.anim_top_out

                    popEnter = R.anim.anim_top_in
                    popExit = R.anim.anim_bottom_out
                }
            }
            NavManager.navigate(NFragmentB::class, null, options)
        }
    }
}