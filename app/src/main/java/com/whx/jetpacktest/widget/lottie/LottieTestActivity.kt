package com.whx.jetpacktest.widget.lottie

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewConfiguration
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.databinding.ActivityTestLottieBinding
import java.lang.ref.WeakReference

class LottieTestActivity : BaseActivity() {
    private lateinit var binding: ActivityTestLottieBinding

    private val countDownTimer = PressCountdown(this)
    private var clickTime = 0L

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestLottieBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.lottieView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                Log.w("-------------", "animator start")
            }

            override fun onAnimationEnd(animation: Animator) {
                Log.w("-------------", "animator end")
            }

            override fun onAnimationCancel(animation: Animator) {
                Log.w("-------------", "animator cancel")
            }

            override fun onAnimationRepeat(animation: Animator) {
                Log.w("-------------", "animator repeat")
            }
        })
        binding.lottieView.addAnimatorUpdateListener {
            Log.w("-------------", "animator fraction:${it.animatedFraction}")
            Log.w("-------------", "animator animatedValue:${it.animatedValue}")
            Log.w("-------------", "animator currentPlayTime:${it.currentPlayTime}")
        }
        binding.pressBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    clickTime = System.currentTimeMillis()
                    v.onTouchEvent(event)
                }
                MotionEvent.ACTION_UP -> {
                    Log.w("-----------", ViewConfiguration.getLongPressTimeout().toString())
                    countDownTimer.cancel()
                    if ((System.currentTimeMillis() - clickTime) > ViewConfiguration.getLongPressTimeout() && !countDownTimer.isFinish) {
                        binding.lottieView.reverseAnimationSpeed()
                        binding.lottieView.resumeAnimation()
                    }
                    v.onTouchEvent(event)
                }
                else -> {
                    v.onTouchEvent(event)
                }
            }
        }

        binding.pressBtn.setOnClickListener {
            Log.w("------------", "click")
        }

        binding.pressBtn.setOnLongClickListener {
            Log.w("------------", "long click")
            if (binding.lottieView.speed < 0) {
                binding.lottieView.reverseAnimationSpeed()
            }
            countDownTimer.startCount()
            true
        }
    }

    class PressCountdown(ref: LottieTestActivity) : CountDownTimer(2000, 10) {
        private val weakRef = WeakReference(ref)

        var isFinish = false

        fun startCount() {
            isFinish = false
            this.start()
        }

        override fun onTick(millisUntilFinished: Long) {
            val progress = (2000 - millisUntilFinished).toFloat() / 2000
            weakRef.get()?.binding?.lottieView?.progress = progress
            Log.w("-------------", progress.toString())
        }

        override fun onFinish() {
            isFinish = true
            Log.w("-------------", "finish")
        }
    }
}