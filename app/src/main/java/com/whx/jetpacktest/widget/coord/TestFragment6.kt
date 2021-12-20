package com.whx.jetpacktest.widget.coord

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.whx.jetpacktest.BaseFragment
import com.whx.jetpacktest.R

class TestFragment6 : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_coord_test6, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appbar = view.findViewById<AppBarLayout>(R.id.appbar_layout)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.bottom = 20
                outRect.right = 20
            }
        })
        recyclerView.adapter = TestFragment2.Adapter()

        val view1 = view.findViewById<View>(R.id.view_may_gone)

        val button = view.findViewById<Button>(R.id.click_show)
        button.setOnClickListener {
            if (view1.visibility != View.VISIBLE) {
                view1.visibility = View.VISIBLE
            } else {
                view1.visibility = View.GONE
            }
        }

        val expand = view.findViewById<Button>(R.id.click_expand)
        expand.setOnClickListener {
            isExpand = if (isExpand) {
                appbar.setExpanded(false)
                false
            } else {
                appbar.setExpanded(true)
                true
            }
        }
    }

    private var isExpand = false
}