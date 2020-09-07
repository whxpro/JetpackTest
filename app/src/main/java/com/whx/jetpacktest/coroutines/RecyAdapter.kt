package com.whx.jetpacktest.coroutines

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whx.jetpacktest.R
import com.whx.jetpacktest.viewmodel.Meizi
import com.whx.jetpacktest.widget.BaseRecyclerAdapter
import com.whx.jetpacktest.widget.imagepick.PreviewActivity
import java.util.*

class RecyAdapter : BaseRecyclerAdapter<Meizi, RecyViewHolder>() {

    override fun onCreateCustomVH(parent: ViewGroup, viewType: Int): RecyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_grid_item, parent, false)

        return RecyViewHolder(view)
    }

    override fun onBindCustomVH(holder: RecyViewHolder, position: Int) {
        holder.bindView(data[position], position)
    }
}

class RecyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val mImage = view.findViewById<ImageView>(R.id.item_image)
    private val mDesc = view.findViewById<TextView>(R.id.item_desc)
    private val mFavorite = view.findViewById<ImageView>(R.id.favorite_btn)

    private val ratios = arrayOf(1f, 0.9f, 1.2f, 1.5f)

    init {
        resetImageSize(mImage)
    }
    private fun resetView() {
        mImage.setImageDrawable(null)
        mDesc.text = ""
        mFavorite.isSelected = false
    }
    fun bindView(mz: Meizi, position: Int) {
        resetView()

        Glide.with(itemView).load(mz.portrait).into(mImage)

        mDesc.text = mz.name
        mFavorite.isSelected = mz.liked

        mImage.setOnClickListener {
            it.context.startActivity(Intent(it.context, PreviewActivity::class.java).apply {
                putExtra(PreviewActivity.PHOTO_PATH, mz.portrait)
            })
        }

        mFavorite.setOnClickListener {
            it.isSelected = !it.isSelected
            mz.liked = !mz.liked
        }
    }

    private fun resetImageSize(view: View) {
        val lp = view.layoutParams
        val width = view.width
        val height = width * (ratios[Random().nextInt(ratios.size)])
        lp.height = height.toInt()

        view.layoutParams = lp
    }
}