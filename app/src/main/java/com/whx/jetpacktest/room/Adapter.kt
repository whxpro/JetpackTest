package com.whx.jetpacktest.room

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whx.jetpacktest.R
import com.whx.jetpacktest.room.entity.User
import com.whx.jetpacktest.room.entity.UserWithPlaylists

class UserAdapter(private val mItemClickListener: ItemClickListener?) : ListAdapter<UserWithPlaylists, UserHolder>(UserComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(parent)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bindData(getItem(position), position, mItemClickListener)
    }
}

class UserHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_user_item, parent, false)) {
    private val mAvatar = itemView.findViewById<ImageView>(R.id.user_avatar)
    private val mNameTv = itemView.findViewById<TextView>(R.id.user_name)
    private val mAgeTv = itemView.findViewById<TextView>(R.id.user_age)
    private val mAddMusicBtn = itemView.findViewById<Button>(R.id.to_add_music)

    fun bindData(data: UserWithPlaylists, position: Int, itemClickListener: ItemClickListener?) {
        Glide.with(itemView.context).load(data.user.avatar).into(mAvatar)
        mNameTv.text = data.user.userName
        mAgeTv.text = data.user.age.toString()

        mAddMusicBtn.setOnClickListener {
            itemClickListener?.onItemClick(data, position)
        }

        itemView.setOnClickListener {
            val str = data.playlists.map { it.playlistName }.takeIf { it.isNotEmpty() }?.reduceRight { s, acc -> "$s%%$acc" } ?: "empty"
            Toast.makeText(itemView.context, str, Toast.LENGTH_SHORT).show()
        }
    }
}
interface ItemClickListener {
    fun onItemClick(item: UserWithPlaylists, position: Int)
}

class UserComparator : DiffUtil.ItemCallback<UserWithPlaylists>() {
    override fun areItemsTheSame(oldItem: UserWithPlaylists, newItem: UserWithPlaylists): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: UserWithPlaylists, newItem: UserWithPlaylists): Boolean {
        return oldItem.user.userId == newItem.user.userId
    }
}
