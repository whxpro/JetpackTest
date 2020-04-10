package com.whx.jetpacktest

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String) = Glide.with(this.context).load(url).into(this)

@SuppressLint("SetTextI18n")
@BindingAdapter("android:text")
fun Button.setBtnText(text: String) {
    this.text = "$text-btn"
}

@BindingConversion
fun convertStringToDrawable(colorStr: String) = when(colorStr) {
    "red" -> ColorDrawable(Color.RED)
    "blue" -> ColorDrawable(Color.BLUE)
    "green" -> ColorDrawable(Color.GREEN)
    else -> ColorDrawable(Color.BLACK)
}

@BindingConversion
fun convertStringToColorInt(colorStr: String) = when(colorStr) {
    "red" -> Color.RED
    "blue" -> Color.BLUE
    "green" -> Color.GREEN
    else -> Color.BLACK
}

@BindingAdapter("adapter")
fun <V : RecyclerView.ViewHolder> RecyclerView.adapter(adapter: RecyclerView.Adapter<V>) {
    this.layoutManager = LinearLayoutManager(this.context)
    this.adapter = adapter
}

@BindingAdapter("autoRefresh")
fun RecyclerView.autoRefresh(auto: Boolean) {
    if (auto) {
        this.adapter?.notifyDataSetChanged()
    }
}