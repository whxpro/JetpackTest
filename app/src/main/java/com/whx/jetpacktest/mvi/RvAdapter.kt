package com.whx.jetpacktest.mvi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whx.jetpacktest.R
import kotlinx.android.synthetic.main.layout_rv_item.view.*

class RvAdapter : ListAdapter<ImageUiState, RvHolder>(MeiziItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvHolder {
        return RvHolder(parent)
    }

    override fun onBindViewHolder(holder: RvHolder, position: Int) {
        holder.bind(getItem(position))
    }

    internal class MeiziItemCallback : DiffUtil.ItemCallback<ImageUiState>() {
        override fun areItemsTheSame(oldItem: ImageUiState, newItem: ImageUiState): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ImageUiState, newItem: ImageUiState): Boolean {
            return oldItem.picsumBean.id == newItem.picsumBean.id
        }
    }
}

class RvHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_rv_item, parent, false)) {

    fun bind(imageUiState: ImageUiState) {
        itemView.run {
            val meizi = imageUiState.picsumBean
            title.text = meizi.author
            subtitle.text = meizi.url
            Glide.with(context).load(meizi.downloadUrl).into(portrait)

            like_btn.isSelected = meizi.liked
            like_btn.setOnClickListener {
                val like = !it.isSelected
                it.isSelected = like
                imageUiState.onClickLike.invoke(meizi.id, like)
            }
        }
    }
}