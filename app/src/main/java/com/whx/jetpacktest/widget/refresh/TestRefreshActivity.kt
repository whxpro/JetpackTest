package com.whx.jetpacktest.widget.refresh

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R
import com.whx.jetpacktest.utils.dp
import com.whx.jetpacktest.utils.toast
import kotlinx.android.synthetic.main.activity_test_refresh.*

class TestRefreshActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_refresh)

        initView()
    }

    private var hasHint = true
    private fun initView() {
        test_list.layoutManager = LinearLayoutManager(this)
        val adapter = TestAdapter()
        test_list.adapter = adapter

        refresh_container.setHeadView(SimpleLoadingHead(this))
        refresh_container.setOnRefreshListener(object : DWRefreshLayout.OnRefreshListener {
            override fun onLoadMore() {
                toast("load more")
            }

            override fun onPull(direction: Int, distance: Int) {
            }

            override fun onRefresh() {
                toast("refresh")
                Handler().postDelayed({
                    toast("complete")
                    refresh_container.refreshComplete()
                    adapter.setHasHint(false)
                    hasHint = false
                }, 1000)
            }
        })

        adapter.setData(arrayListOf("hhhhhhh", "lllllllll", "dddddd", "gggggggg", "kkkkkkkkk", "aaaaaaaaaa", "bbbbbbbbb", "cccccccc", "eeeeeeeee", "fffffffff", "iiiiiiiii"))

        change.setOnClickListener {
            if (hasHint) {
                adapter.setHasHint(false)
            } else {
                adapter.setHasHint(true)
            }
            hasHint = !hasHint
        }
    }
}

class TestAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: MutableList<String>? = null

    private var hasHint = true

    override fun getItemCount(): Int {
        return if (hasHint) {
            (data?.size ?: 0) + 1
        } else {
            data?.size ?: 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (hasHint && viewType == 233) {
            HintViewHolder(ImageView(parent.context).apply {
                setImageResource(R.mipmap.ic_launcher)
            })
        } else {
            TestViewHolder(TextView(parent.context).apply {
                layoutParams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30.dp().toInt())
                gravity = Gravity.CENTER
            })
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasHint && position == 0) {
            233
        } else {
            666
        }
    }

    fun setHasHint(hasHint: Boolean) {
        this.hasHint = hasHint
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (!data.isNullOrEmpty()) {
            if (holder is TestViewHolder) {
                if (hasHint && (position - 1) in data!!.indices) {
                    holder.bindView(data!![position - 1])
                } else if (position in data!!.indices) {
                    holder.bindView(data!![position])
                }
            } else {

            }
        }
    }

    fun setData(data: List<String>) {
        if (this.data == null) {
            this.data = arrayListOf()
            this.data!!.addAll(data)
        } else {
            this.data!!.clear()
            this.data!!.addAll(data)
        }
        notifyDataSetChanged()
    }
}
class HintViewHolder(view: View) : RecyclerView.ViewHolder(view) {

}
class TestViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bindView(text: String) {
        if (itemView is TextView) {
            itemView.text = text
        }
    }
}