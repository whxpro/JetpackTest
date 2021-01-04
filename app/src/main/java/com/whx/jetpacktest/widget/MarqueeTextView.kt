package com.whx.jetpacktest.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.Scroller
import androidx.appcompat.widget.AppCompatTextView
import com.whx.jetpacktest.R

class MarqueeTextView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defS: Int = 0
) : AppCompatTextView(ctx, attrs, defS) {

    companion object {
        const val SCROLL_TIME_DFLT = 5000
        const val SCROLL_COUNT_DFLT = 1
        const val SCROLL_COUNT_INF = -1
        const val SCROLL_TIME_INTERVAL = 500
    }

    private var mScroller: Scroller? = null
    private var mScrollTime = SCROLL_TIME_DFLT
    private var mPausedX = 0
    private var mPaused = true
    private var mFirst = true
    private var mScrollCount = SCROLL_COUNT_DFLT
    private var mScrollInterval = SCROLL_TIME_INTERVAL
    private var mAutoScroll = true

    init {
        initAttrs(ctx, attrs)
        setSingleLine()
    }

    private fun initAttrs(ctx: Context, attrs: AttributeSet?) {
        if (attrs == null) return
        var tpa: TypedArray? = null
        try {
            tpa = ctx.obtainStyledAttributes(attrs, R.styleable.MarqueeTextView)
            mScrollTime =
                tpa!!.getInt(R.styleable.MarqueeTextView_mtv_scroll_time, SCROLL_TIME_DFLT)
            mScrollCount =
                tpa.getInt(R.styleable.MarqueeTextView_mtv_scroll_count, SCROLL_COUNT_DFLT)
            mScrollInterval =
                tpa.getInt(R.styleable.MarqueeTextView_mtv_scroll_interval, SCROLL_TIME_INTERVAL)
            mAutoScroll = tpa.getBoolean(R.styleable.MarqueeTextView_mtv_auto_scroll, true)
        } finally {
            tpa?.recycle()
        }
    }

    fun startScroll() {
        mPausedX = 0
        mPaused = true
        mFirst = true
        resumeScroll()
    }

    fun resumeScroll() {
        if (!mPaused) return
        setHorizontallyScrolling(true)

        if (mScroller == null) {
            mScroller = Scroller(context, LinearInterpolator())
            setScroller(mScroller)
        }

        val scrollLen = calculateScrollingLen()
        val dis = scrollLen - mPausedX
        val duration = (mScrollTime * (dis.toFloat() / scrollLen)).toInt()

        if (mFirst) {
            postDelayed({
                mScroller?.startScroll(mPausedX, 0, dis, 0, duration)
                invalidate()
                mPaused = false
            }, 1000)
        } else {
            mScroller?.startScroll(mPausedX, 0, dis, 0, duration)
            invalidate()
            mPaused = false
        }
    }

    fun pauseScroll() {
        if (mScroller == null || mPaused) return
        mPaused = true
        mPausedX = mScroller!!.currX
        mScroller!!.abortAnimation()
    }

    fun stopScroll() {
        mScroller?.run {
            mPaused = true
            startScroll(0, 0, 0, 0, 0)
        }
    }

    private fun calculateScrollingLen(): Int {
        val rect = Rect()
        val text = text.toString()
        paint.getTextBounds(text, 0, text.length, rect)
        return rect.width()
    }

    override fun computeScroll() {
        super.computeScroll()
        mScroller?.run {
            if (currX >= calculateScrollingLen() - width + 200) {
                forceFinished(true)
//                stopScroll()
                mPaused = true
                mPausedX = -width
                mFirst = false
                resumeScroll()
            }
//            if (isFinished && !mPaused) {
//                fun scrollAgain() {
//                    mPaused = true
//                    mPausedX = -width
//                    mFirst = false
//                    resumeScroll()
//                }
//                if (mScrollCount == SCROLL_COUNT_INF) {
//                    scrollAgain()
//                    return
//                }
//                if (--mScrollCount > 0) {
//                    scrollAgain()
//                    return
//                }
//                stopScroll()
//            }
        }
    }
}