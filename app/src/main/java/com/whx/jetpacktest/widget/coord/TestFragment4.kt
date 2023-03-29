package com.whx.jetpacktest.widget.coord

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.whx.jetpacktest.BaseFragment
import com.whx.jetpacktest.R
import com.whx.jetpacktest.utils.CommonUtils

class TestFragment4 : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_coord_test4_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("-------", "screen h: ${CommonUtils.getScreenHeight()}")

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        /*val lp = recyclerView.layoutParams
        lp.height = CommonUtils.getScreenHeight()
        recyclerView.layoutParams = lp*/
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val lp = view.layoutParams
                if (lp is StaggeredGridLayoutManager.LayoutParams) {
                    if (lp.isFullSpan) {
                        outRect.bottom = 20
                        outRect.right = 0
                    } else {
                        outRect.bottom = 20
                        outRect.right = 20
                    }
                }
            }
        })
        recyclerView.adapter = Adapter()
    }

    class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val set = mutableSetOf<String>()
        private val strings = arrayListOf<String>()

        init {
            for (i in (0..20)) {
                strings.add("item# $i")
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when {
                position == 6 -> 101
                position < 8 -> position
                else -> 100
            }
        }

        override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
            super.onViewAttachedToWindow(holder)
            val lp = holder.itemView.layoutParams
            if (lp is StaggeredGridLayoutManager.LayoutParams) {
                lp.isFullSpan = holder is SimpleVH
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                100 -> TestFragment2.ViewHolder(parent)
                101 -> WebViewHolder(parent)
                else -> SimpleVH(parent)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is TestFragment2.ViewHolder -> {
                    if ((position - 8) in strings.indices) {
                        holder.bind(strings[position - 8])
                    }
                }
                is SimpleVH -> {
                    holder.bind()
                }
                is WebViewHolder -> {
                    holder.bindUrl("www.baidu.com")
                }
            }
            val id = "item# $position"
            if (!set.contains(id)) {
                Log.e("-------", "bind $id")
                set.add(id)
            }
        }

        override fun getItemCount(): Int {
            return 28
        }
    }

    class SimpleVH(parent: ViewGroup) : RecyclerView.ViewHolder(View(parent.context).apply {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (Math.random() * 500).toInt())
        background = ColorDrawable(
            Color.rgb(
                (Math.random() * 255).toInt(),
                (Math.random() * 255).toInt(),
                (Math.random() * 255).toInt()
            )
        )
    }) {
        fun bind() {

        }
    }

    class WebViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(WebView(parent.context).apply {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url?.toString() ?: "")
                return true
            }
        }
    }) {
        fun bindUrl(url: String) {
            val v = itemView
            if (v is WebView) {
                v.loadUrl(url)
            }
        }
    }
}