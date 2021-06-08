package com.whx.jetpacktest.widget.coord

import android.os.Bundle
import android.view.View
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R
import kotlinx.android.synthetic.main.activity_coord_test.*

class CoordTestActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_coord_test)

        findViewById<View>(R.id.fragment_container).bringToFront()

        to_first.setOnClickListener {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, TestFragment1()).commit()
        }
    }
}