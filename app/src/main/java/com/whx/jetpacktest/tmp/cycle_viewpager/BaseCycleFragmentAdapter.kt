package com.whx.jetpacktest.tmp.cycle_viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder

abstract class BaseCycleFragmentAdapter : FragmentStateAdapter {

    constructor(fragmentActivity: FragmentActivity): super(fragmentActivity)

    constructor(fragment: Fragment): super(fragment)

    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle): super(fragmentManager, lifecycle)

    override fun getItemCount(): Int {
        return if (getRealItemCount() > 1) getRealItemCount() + 2 else getRealItemCount()
    }

    override fun createFragment(position: Int): Fragment {
        return createRealFragment(getRealPosition(position, getRealItemCount()))
    }

    override fun onBindViewHolder(holder: FragmentViewHolder, position: Int, payloads: MutableList<Any>) {
        onBindRealViewHolder(holder, position, payloads)
    }
    override fun getItemViewType(position: Int): Int {
        return getRealItemViewType(getRealPosition(position, getRealItemCount()))
    }

    override fun getItemId(position: Int): Long {
        return getRealItemId(getRealPosition(position, getRealItemCount()))
    }

    abstract fun createRealFragment(position: Int): Fragment
    abstract fun getRealItemCount(): Int

    fun onBindRealViewHolder(holder: FragmentViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    fun getRealItemViewType(position: Int): Int {
        return 0
    }

    fun getRealItemId(position: Int): Long {
        return RecyclerView.NO_ID
    }

    fun getRealPosition(position: Int, itemCount: Int): Int {
        return when (position) {
            0 -> itemCount - 1
            itemCount + 1 -> 0
            else -> position - 1
        }
    }
}