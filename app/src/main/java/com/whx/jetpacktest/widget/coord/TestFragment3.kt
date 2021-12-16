package com.whx.jetpacktest.widget.coord

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.whx.jetpacktest.BaseFragment
import com.whx.jetpacktest.R
import java.lang.Exception

class TestFragment3 : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_coord_test3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
    }

    private fun initView(view: View) {
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)

        tabLayout.addTab(getTab(tabLayout, "What"))
        tabLayout.addTab(getTab(tabLayout, "The"))
        tabLayout.addTab(getTab(tabLayout, "Fuck"))
        tabLayout.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                viewPager.currentItem = p0?.position ?: 0
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }
        })
        viewPager.adapter = PagerAdapter(this)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.getTabAt(position)?.select()
            }
        })
    }

    private fun getTab(tabLayout: TabLayout, title: String): TabLayout.Tab {
        try {
            val tab = tabLayout.newTab()
            tab.text = title

            return tab
        } catch (e: Exception) {
            Log.e("-----", "", e)
            throw e
        }
    }

    class PagerAdapter(parent: Fragment) : FragmentStateAdapter(parent) {
        override fun getItemCount() = 3

        override fun createFragment(position: Int): Fragment {
            return TabFragment()
        }
    }

    class TabFragment : BaseFragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.activity_recycler, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.bottom = 20
                    outRect.right = 20
                }
            })
            recyclerView.adapter = TestFragment2.Adapter()
        }
    }
}