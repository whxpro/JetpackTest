package com.whx.jetpacktest.widget.coord

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.whx.jetpacktest.BaseFragment
import com.whx.jetpacktest.R
import com.whx.jetpacktest.utils.CommonUtils
import com.whx.jetpacktest.utils.dp

class TestFragment2 : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_coord_test2, container, false)
    }

    private var mAppbarOffset = 0

    private var mDecorH = 0
    private var mMaxH = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDecorH = activity?.window?.decorView?.height ?: 0
        mMaxH = mDecorH - 50.dp().toInt()

        val appbar = view.findViewById<AppBarLayout>(R.id.appbar_layout)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.bottom = 20
                outRect.right = 20
            }
        })

        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbarLayout, offset ->
            if (appbarLayout.height > 0 && mAppbarOffset != offset) {
                val lp = recyclerView.layoutParams
                val h = mMaxH - (appbarLayout.totalScrollRange + offset)

                if (lp.height < mMaxH) {
                    lp.height = h
                    recyclerView.layoutParams = lp
                }
                mAppbarOffset = offset
            }
        })

        appbar.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                appbar.viewTreeObserver.removeOnPreDrawListener(this)
                val rect = Rect()
                recyclerView.getGlobalVisibleRect(rect)

                val lp = recyclerView.layoutParams
                lp.height = mDecorH - appbar.height
                recyclerView.layoutParams = lp

                recyclerView.adapter = Adapter()
                Log.e("---------", "screen h: ${CommonUtils.getScreenHeight()}, recycler h: ${recyclerView.height}, vis: $rect, vis h: ${rect.bottom - rect.top}")
                return true
            }
        })
    }

    class Adapter : RecyclerView.Adapter<ViewHolder>() {
        private val set = mutableSetOf<String>()
        private val strings = arrayListOf<String>()

        init {
            for (i in (0..20)) {
                strings.add("item# $i")
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (position in strings.indices) {
                val text = strings[position]
                holder.bind(text)
                if (!set.contains(text)) {
                    Log.e("----------", "bind: $text")
                    set.add(text)
                }
            }
        }

        override fun getItemCount() = strings.size
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(TextView(parent.context).apply {
        val lp = ViewGroup.LayoutParams(100.dp().toInt(), 150.dp().toInt())
        setBackgroundColor(android.graphics.Color.GREEN)
        layoutParams = lp
        gravity = Gravity.CENTER
        textSize = 15f
    }) {

        fun bind(text: String) {
            val v = itemView
            if (v is TextView) {
                v.text = text
            }
        }
    }
}