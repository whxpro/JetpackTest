package com.whx.jetpacktest.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.whx.jetpacktest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ScrollTextView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defS: Int = 0
) : FrameLayout(ctx, attrs, defS) {

    private val textList = arrayListOf<String>()

    private val tv1 = TextView(ctx)
    private val tv2 = TextView(ctx)

    private val animDuration = 1000L
    private var deltaTime = 1000L
    private var cursor = 0

    private var exitView: TextView? = null
    private var enterView: TextView? = null


    private val exitAlphaAnim = ValueAnimator.ofFloat(1f, 0f).apply {
        duration = animDuration
        addUpdateListener {
            Log.e("--------", it.animatedValue.toString())
            exitView?.alpha = it.animatedValue as Float
        }
    }
    private lateinit var exitTransAnim: ValueAnimator

    private val exitSet = AnimatorSet()

    init {
        ViewTreeLifecycleOwner.set(this, context as? LifecycleOwner)
        initTextView()

    }

    private fun initTextView() {
        addView(tv1)
        addView(tv2)


        exitTransAnim = ValueAnimator.ofFloat(0f, -(tv1.height.toFloat() + 100)).apply {
            duration = animDuration
            addUpdateListener {
                exitView?.translationY = it.animatedValue as Float
            }
        }
//        exitTransAnim.doOnEnd {
//            exitView = tv2
//        }
    }

    private fun startScroll() {
        findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            Toast.makeText(context, "coroutine scope thread#${Thread.currentThread().name}", Toast.LENGTH_SHORT).show()
            tv1.text = textList[0]
            tv2.text = textList[1]
            exitView = tv1

            delay(deltaTime)
            exitSet.play(exitTransAnim).with(exitAlphaAnim)
            exitSet.start()
        }
    }

    fun setTextList(texts: List<String>) {
        if (texts.isNotEmpty()) {
            this.textList.clear()
            this.textList.addAll(texts)
            startScroll()
        }
    }
}