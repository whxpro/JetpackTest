package com.whx.jetpacktest.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ClipGuideView @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null, defS: Int = 0) :
    View(ctx, attrs, defS) {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#66000000")
    }

    private val mRect = RectF(0f, 0f, 0f, 0f)
    private val mRoundRect = RectF(0f, 0f, 0f, 0f)

    private val mPath = Path()
    private val mPath1 = Path()
    private val mPath2 = Path()

    private var mRadius = 0f
    private var mClipX = 0f
    private var mClipY = 0f

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null)
            return

        mRect.right = width.toFloat()
        mRect.bottom = height.toFloat()

        mPath1.addRect(mRect, Path.Direction.CW)
        mPath2.addRoundRect(mRoundRect, mRadius, mRadius, Path.Direction.CW)

        mPath.op(mPath1, mPath2, Path.Op.DIFFERENCE)

        canvas.drawPath(mPath, mPaint)
    }

    fun setClipCenter(x: Float, y: Float) {
        mClipX = x
        mClipY = y
    }

    fun setCornerRadius(radius: Float) {
        mRadius = radius
    }

    fun setArea(w: Float, h: Float) {
        mRoundRect.left = mClipX - (w / 2)
        mRoundRect.right = mClipX + (w / 2)
        mRoundRect.top = mClipY - (h / 2)
        mRoundRect.bottom = mClipY + (h /2)
    }

    fun update() {
        mPath1.reset()
        mPath2.reset()
        mPath.reset()
        invalidate()
    }
}