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
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, TestFragment1())
                .addToBackStack("")
                .commit()
        }

        to_second.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_right_in, R.anim.anim_right_out)
                .add(R.id.fragment_container, TestFragment2())
                .addToBackStack("")
                .commit()
        }

        to_third.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_right_in, R.anim.anim_right_out)
                .add(R.id.fragment_container, TestFragment3())
                .addToBackStack("")
                .commit()
        }

        to_fourth.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_right_in, R.anim.anim_right_out)
                .add(R.id.fragment_container, TestFragment4())
                .addToBackStack("")
                .commit()
        }

        to_fifth.setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.anim_right_in, R.anim.anim_right_out)
                    .add(R.id.fragment_container, TestFragment5())
                    .addToBackStack("")
                    .commit()
        }

        to_sixth.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_right_in, R.anim.anim_right_out)
                .add(R.id.fragment_container, TestFragment6())
                .addToBackStack("")
                .commit()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.isNotEmpty()) {
            supportFragmentManager.popBackStack()
            return
        }
        super.onBackPressed()
    }

    override fun isLightTheme() = true
}