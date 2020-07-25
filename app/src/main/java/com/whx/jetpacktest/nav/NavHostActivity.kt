package com.whx.jetpacktest.nav

import android.os.Bundle
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R

class NavHostActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)

        NavManager.init(this, R.id.nav_host_fragment)
    }
}