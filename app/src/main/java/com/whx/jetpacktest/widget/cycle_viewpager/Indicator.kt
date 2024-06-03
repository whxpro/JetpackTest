package com.whx.jetpacktest.widget.cycle_viewpager

import android.view.View

interface Indicator {

    fun onDataChanged(itemCount: Int, currentPosition: Int)

    fun onPageSelected(position: Int)

    fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)

    fun onPageScrollStateChanged(state: Int)
}