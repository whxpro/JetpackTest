package com.whx.jetpacktest.widget.statusbar

import android.content.Intent
import android.os.Bundle
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R
import kotlinx.android.synthetic.main.fragment_nav_common.*

class ActivityBlue : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_nav_common)

        center_text.setOnClickListener {
            startActivity(Intent(this, ActivityBlack::class.java))
        }
    }
}