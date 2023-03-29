package com.whx.jetpacktest.widget.statusbar

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R
import com.whx.jetpacktest.utils.StatusBarUtil

class ActivityBlack : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_nav_common)

        findViewById<View>(R.id.center_text).setOnClickListener {
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