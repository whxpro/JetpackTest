package com.whx.jetpacktest.widget.statusbar

import android.graphics.Color
import android.os.Bundle
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R
import com.whx.jetpacktest.utils.StatusBarUtil

class ActivityWhite : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_nav_common)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        StatusBarUtil.setStatusBarColor(window, Color.WHITE)
    }
}