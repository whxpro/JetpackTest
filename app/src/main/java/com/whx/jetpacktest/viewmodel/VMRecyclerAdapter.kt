package com.whx.jetpacktest.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.whx.jetpacktest.R
import com.whx.jetpacktest.databinding.RecyclerItemBinding

class VMRecyclerAdapter(private val data: List<Map<String, Any>>?): RecyclerView.Adapter<VMViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VMViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<RecyclerItemBinding>(
            LayoutInflater.from(parent.context), R.layout.recycler_item, parent, false)

        return VMViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: VMViewHolder, position: Int) {
        if (!data.isNullOrEmpty() && position in data.indices) {
            val meizi = Meizi("meizi#$position", data[position]["url"].toString(), false)
            holder.bindView(meizi)
        }
    }

    override fun getItemCount(): Int {
        return data?.size ?:0
    }
}

class VMViewHolder(private val viewDataBinding: RecyclerItemBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {

    fun bindView(meizi: Meizi) {
        viewDataBinding.meizi = meizi
    }
}