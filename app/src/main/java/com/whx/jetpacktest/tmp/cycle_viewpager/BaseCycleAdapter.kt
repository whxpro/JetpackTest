package com.whx.jetpacktest.tmp.cycle_viewpager

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_ID

abstract class BaseCycleAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    override fun getItemCount(): Int {
        return if (getRealItemCount() > 1) getRealItemCount() + 2 else getRealItemCount()
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindRealViewHolder(
            holder,
            getRealPosition(position, getRealItemCount())
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        onBindRealViewHolder(
            holder,
            getRealPosition(position, getRealItemCount()),
            payloads
        )
    }

    override fun getItemViewType(position: Int): Int {
        return getRealItemViewType(getRealPosition(position, getRealItemCount()))
    }

    override fun getItemId(position: Int): Long {
        return getRealItemId(getRealPosition(position, getRealItemCount()))
    }

    abstract fun getRealItemCount(): Int
    abstract fun onBindRealViewHolder(holder: VH, position: Int)

    fun onBindRealViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    fun getRealItemViewType(position: Int): Int {
        return 0
    }
    fun getRealItemId(position: Int): Long {
        return NO_ID
    }

    fun getRealPosition(position: Int, itemCount: Int): Int {
        return when (position) {
            0 -> itemCount - 1
            itemCount + 1 -> 0
            else -> position - 1
        }
    }
}