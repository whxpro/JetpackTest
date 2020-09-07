package com.whx.jetpacktest.widget

import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<D, VH: RecyclerView.ViewHolder> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val HEADER = 100
        const val FOOTER = 101
    }
    var data = arrayListOf<D>()
        private set

    private var hasHeader = false
    private var hasFooter = false

    override fun getItemCount(): Int {
        if (hasHeader && hasFooter) {
            return data.size + 2
        }
        if (hasHeader || hasFooter) {
            return data.size + 1
        }
        return data.size
    }

    @CallSuper
    override fun getItemViewType(position: Int): Int {
        if (position == 0 && hasHeader) {
            return HEADER
        }
        if (position == itemCount - 1 && hasFooter) {
            return FOOTER
        }
        return super.getItemViewType(position)
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> {
                createHeaderVH(parent)
            }
            FOOTER -> {
                createFooterVH(parent)
            }
            else -> {
                onCreateCustomVH(parent, viewType)
            }
        }
    }

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == HEADER && holder is BaseHFViewHolder) {
            holder.bindHeadView()
        } else if (getItemViewType(position) == FOOTER && holder is BaseHFViewHolder) {
            holder.bindFootView()
        } else {
            onBindCustomVH((holder as VH), if (hasHeader) position - 1 else position)
        }
    }

    abstract fun onCreateCustomVH(parent: ViewGroup, viewType: Int): VH
    abstract fun onBindCustomVH(holder: VH, position: Int)

    protected open fun createHeaderVH(parent: ViewGroup): BaseHFViewHolder {
        return getDefaultHFViewHolder(parent)
    }

    protected open fun createFooterVH(parent: ViewGroup): BaseHFViewHolder {
        return getDefaultHFViewHolder(parent)
    }
    private fun getDefaultHFViewHolder(parent: ViewGroup): BaseHFViewHolder {
        return object : BaseHFViewHolder(View(parent.context)) {
            override fun bindHeadView() {
            }

            override fun bindFootView() {
            }
        }
    }

    fun setData(data: List<D>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun addData(data: List<D>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun addItem(idx: Int? = null, data: D) {
        if (idx == null) {
            this.data.add(data)
            notifyItemInserted(this.data.size - 1)
        } else {
            this.data.add(idx, data)
            notifyItemInserted(idx)
        }
    }

    fun removeItem(position: Int) {
        this.data.removeAt(position)
        notifyItemRangeRemoved(position, 1)
    }

    fun clearData() {
        this.data.clear()
        notifyDataSetChanged()
    }
}
abstract class BaseHFViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bindHeadView()
    abstract fun bindFootView()
}