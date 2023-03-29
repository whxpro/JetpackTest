package com.whx.jetpacktest.widget.coord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R
import com.whx.jetpacktest.databinding.ActivityCoordTestBinding

class CoordTestActivity : BaseActivity() {
    private lateinit var binding: ActivityCoordTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoordTestBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        findViewById<View>(R.id.fragment_container).bringToFront()

        binding.toFirst.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, TestFragment1())
                .addToBackStack("")
                .commit()
        }

        binding.toSecond.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_right_in, R.anim.anim_right_out)
                .add(R.id.fragment_container, TestFragment2())
                .addToBackStack("")
                .commit()
        }

        binding.toThird.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_right_in, R.anim.anim_right_out)
                .add(R.id.fragment_container, TestFragment3())
                .addToBackStack("")
                .commit()
        }

        binding.toFourth.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_right_in, R.anim.anim_right_out)
                .add(R.id.fragment_container, TestFragment4())
                .addToBackStack("")
                .commit()
        }

        binding.toFifth.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_right_in, R.anim.anim_right_out)
                .add(R.id.fragment_container, TestFragment5())
                .addToBackStack("")
                .commit()
        }

        binding.toSixth.setOnClickListener {
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