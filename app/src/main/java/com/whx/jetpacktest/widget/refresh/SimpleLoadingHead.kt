package com.whx.jetpacktest.widget.refresh

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import com.whx.jetpacktest.R
import com.whx.jetpacktest.utils.dp

class SimpleLoadingHead @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null, defA: Int = 0)
    : FrameLayout(ctx, attrs, defA), SimpleRefreshLoad {

    private lateinit var animation: ObjectAnimator
    private val loadingIcon: ImageView

    init {
        LayoutInflater.from(ctx).inflate(R.layout.layout_head_refresh, this, true)
        loadingIcon = findViewById(R.id.loading_icon)
        initAnim()
    }

    private fun initAnim() {
        animation = ObjectAnimator.ofFloat(loadingIcon, "rotation", 0f, 360f)
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