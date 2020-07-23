package com.whx.jetpacktest.widget.cycle_viewpager

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R


class ViewpagerActivity : BaseActivity() {

    private var banner: CycleViewPager2? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpager)

        banner = findViewById(R.id.viewpager2)
        val data = arrayListOf("http://ww1.sinaimg.cn/large/0065oQSqly1fsfq1ykabxj30k00pracv.jpg", "http://ww1.sinaimg.cn/large/0065oQSqly1fsfq2pwt72j30qo0yg78u.jpg",
            "http://ww1.sinaimg.cn/large/0065oQSqly1fsb0lh7vl0j30go0ligni.jpg")
        val adapter = MyAdapter(data)
        banner?.setAdapter(adapter)
        banner?.setAutoTurning(3000)
    }

    class MyAdapter(private val images: List<String>) : BaseCycleAdapter<MyViewHolder>() {
        override fun getRealItemCount(): Int {
            return images.size
        }

        override fun onBindRealViewHolder(holder: MyViewHolder, position: Int) {
            holder.bindView(images[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = ImageView(parent.context)
            val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            view.layoutParams = lp
            return MyViewHolder(view)
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(url: String) {
            Glide.with(itemView).load(url).into(itemView as ImageView)
        }
    }
}