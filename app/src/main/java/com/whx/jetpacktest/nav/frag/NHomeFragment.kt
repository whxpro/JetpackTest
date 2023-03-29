package com.whx.jetpacktest.nav.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navOptions
import com.whx.jetpacktest.BaseFragment
import com.whx.jetpacktest.R
import com.whx.jetpacktest.databinding.FragmentNavHomeBinding
import com.whx.jetpacktest.nav.NavManager

class NHomeFragment : BaseFragment() {
    private lateinit var binding: FragmentNavHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNavHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toFragA.setOnClickListener {
            NavManager.navigate(NFragmentA::class)
        }
        binding.toFragB.setOnClickListener {
            val options = navOptions {
                anim {
                    enter = R.anim.anim_bottom_in                   // fragment b 的进入动画
                    exit = R.anim.anim_top_out                      // home fragment 的退出动画

                    popEnter = R.anim.anim_top_in                   // 从 fragment b 返回时 home fragment 的进入动画
                    popExit = R.anim.anim_bottom_out                // fragment b 的退出动画
                }
            }
            NavManager.navigate(NFragmentB::class, null, options)
        }
    }
}