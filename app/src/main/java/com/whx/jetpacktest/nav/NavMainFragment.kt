package com.whx.jetpacktest.nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.whx.jetpacktest.BaseFragment
import com.whx.jetpacktest.R
import com.whx.jetpacktest.nav.adapter.FragAdapter
import kotlinx.android.synthetic.main.fragment_nav_main.*

class NavMainFragment : BaseFragment() {

    private var prePosition = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nav_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pager_container.adapter = FragAdapter(this)

        setupBottomNav()
    }

    private fun setupBottomNav() {
        bottom_nav.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED   // 菜单标题一直显示
        bottom_nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> pager_container.setCurrentItem(FragAdapter.HOME, false)
                R.id.explore -> pager_container.setCurrentItem(FragAdapter.EXPLORE, false)
                R.id.favorite -> pager_container.setCurrentItem(FragAdapter.FAVORITE, false)
                R.id.person -> pager_container.setCurrentItem(FragAdapter.PERSON, false)
            }
            true
        }
        prePosition = pager_container.currentItem
        pager_container.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottom_nav.menu.getItem(position).isChecked = true
            }
        })
    }
}