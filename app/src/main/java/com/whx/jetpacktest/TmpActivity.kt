package com.whx.jetpacktest

import android.os.Build
import android.os.Bundle
import android.view.View

class TmpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)
    }

    override fun onContentChanged() {
        super.onContentChanged()

        var systemUiVisibility = (View.SYSTEM_UI_FLAG_VISIBLE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        this.window.decorView.systemUiVisibility = systemUiVisibility
    }
}