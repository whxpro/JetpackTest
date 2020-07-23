package com.whx.jetpacktest.widget.refresh

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.whx.jetpacktest.R
import com.whx.jetpacktest.utils.dp
import kotlinx.android.synthetic.main.layout_head_refresh.view.*

class SimpleLoadingHead @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null, defA: Int = 0)
    : FrameLayout(ctx, attrs, defA), SimpleRefreshLoad {

    private lateinit var animation: ObjectAnimator
    init {
        LayoutInflater.from(ctx).inflate(R.layout.layout_head_refresh, this, true)

        initAnim()
    }

    private fun initAnim() {
        animation = ObjectAnimator.ofFloat(loading_icon, "rotation", 0f, 360f)
        animation.duration = 1000
        animation.interpolator = LinearInterpolator()
        animation.repeatCount = ValueAnimator.INFINITE
    }

    override fun onTriggerRefresh() {
        animation.start()
    }

    override fun headViewHeight(): Int {
        return 60.dp().toInt()
    }

    override fun onStop() {
        animation.cancel()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animation.cancel()
    }
}