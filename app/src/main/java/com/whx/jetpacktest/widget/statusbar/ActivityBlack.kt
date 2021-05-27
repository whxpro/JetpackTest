package com.whx.jetpacktest.widget.statusbar

import android.app.ActivityManager
import android.content.Context
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

        val actM = getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        actM?.getRunningTasks(Int.MAX_VALUE)?.forEach {
            println("id: ${it.id}, top: ${it?.topActivity?.className}")
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        StatusBarUtil.setStatusBarColor(window, Color.BLACK)
    }
}