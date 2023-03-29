package com.whx.jetpacktest.nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.whx.jetpacktest.BaseFragment
import com.whx.jetpacktest.R
import com.whx.jetpacktest.databinding.FragmentNavMainBinding
import com.whx.jetpacktest.nav.adapter.FragAdapter

class NavMainFragment : BaseFragment() {
    private lateinit var binding: FragmentNavMainBinding
    private var prePosition = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNavMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pagerContainer.adapter = FragAdapter(this)

        setupBottomNav()
    }

    private fun setupBottomNav() {
        binding.bottomNav.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED   // 菜单标题一直显示
        binding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> binding.pagerContainer.setCurrentItem(FragAdapter.HOME, false)
                R.id.explore -> binding.pagerContainer.setCurrentItem(FragAdapter.EXPLORE, false)
                R.id.favorite -> binding.pagerContainer.setCurrentItem(FragAdapter.FAVORITE, false)
                R.id.person -> binding.pagerContainer.setCurrentItem(FragAdapter.PERSON, false)
            }
            true
        }
        prePosition = binding.pagerContainer.currentItem
        binding.pagerContainer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomNav.menu.getItem(position).isChecked = true
            }
        })
    }
}