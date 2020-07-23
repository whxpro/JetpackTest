package com.whx.jetpacktest.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.whx.jetpacktest.R

class CursorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defArr: Int = 0) : FrameLayout(context, attrs,defArr) {

    private val thumb: View
    private var downX = 0f

    init {
        View.inflate(context, R.layout.layout_cursor, this)
        thumb  = findViewById<View>(R.id.thumb)
        initThumb()
    }

    private fun initThumb() {
        thumb.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.e("-------down, ", event.x.toString())
                    downX = event.x
                }
                MotionEvent.ACTION_MOVE -> {
                    Log.e("-------move, ", event.x.toString())
                    Log.e("-------move, left=", thumb.left.toString())
//                    thumb.x = event.x
                    val lp = (thumb.layoutParams as LayoutParams)
                    var left = lp.leftMargin
                    left += (event.x - downX).toInt()
                    lp.leftMargin = left
                    thumb.layoutParams = lp
                }
            }
            true
        }
    }
}