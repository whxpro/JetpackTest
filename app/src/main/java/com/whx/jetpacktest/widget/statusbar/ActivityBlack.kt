package com.whx.jetpacktest.widget.statusbar

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R
import com.whx.jetpacktest.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_nav_common.*

class ActivityBlack : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_nav_common)

        center_text.setOnClickListener {
            startActivity(Intent(this, ActivityWhite::class.java))
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        StatusBarUtil.setStatusBarColor(window, Color.BLACK)
    }
}