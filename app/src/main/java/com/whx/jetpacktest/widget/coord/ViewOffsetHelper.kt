package com.whx.jetpacktest.widget.coord

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.view.ViewCompat

class ViewOffsetHelper(private val mView: View) {
    private var layoutTop = 0
    private var layoutLeft = 0
    private var offsetTop = 0
    private var offsetLeft = 0

    fun onViewLayout() {
        layoutTop = mView.top
        layoutLeft = mView.left
    }

    fun setTopAndBottomOffset(offset: Int) {
        if (offsetTop != offset) {
            offsetTop = offset
            Log.e("---------", "view: ${mView.contentDescription} offset: $offsetTop")
            ViewCompat.offsetTopAndBottom(mView, offset)
        }
    }

    fun setLeftAndRightOffset(offset: Int) {
        if (offsetLeft != offset) {
            offsetLeft = offset
            ViewCompat.offsetLeftAndRight(mView, offsetLeft - (mView.left - layoutLeft))
        }
    }
}