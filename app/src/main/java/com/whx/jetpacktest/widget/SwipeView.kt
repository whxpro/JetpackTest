package com.whx.jetpacktest.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import com.whx.jetpacktest.R
import java.lang.IllegalStateException
import kotlin.math.abs
import kotlin.math.max

class SwipeView @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null, defS: Int = 0) : FrameLayout(ctx, attrs, defS) {

    private companion object {
        private const val TAG = "SwipeView"

        private const val SWIPE_BOTH = 3
        private const val SWIPE_RIGHT = 2
        private const val SWIPE_LEFT = 1
    }

    private var contentView: View? = null
    private var leftView: View? = null
    private var rightView: View? = null

    private var contentViewWidth = 0; private var contentViewHeight = 0
    private var rightViewWidth = 0; private var rightViewHeight = 0
    private var leftViewWidth = 0; private var leftViewHeight = 0

    private var mStatus: SwipeStatus? = null
    private var mSwipeType = SWIPE_RIGHT

    /**
     * ViewDragHelper类封装了对触摸位置、速度、距离的检测，以及Scroller.
     * 需要我们制定什么时候滑动，以及滑动多少。
     * 需要把ViewGroup中受到的触摸事件传给ViewDragHelper实例
     */
    private val mViewDragHelper: ViewDragHelper
    private val dragCallback = object : ViewDragHelper.Callback() {

        /**
         * @return 返回true表示获得view的控制权
         */
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child == contentView || child == leftView || child == rightView
        }

        /**
         * 控制view在水平方向上实际滑动了多少
         * @param child 当前触摸的view
         * @param left view的左边坐标，负数表示view的左边超出父view边界的长度
         * @param dx
         * @return 返回多少，代表想让child的left=多少
         */
        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            var resLeft = left
            when (child) {
                contentView -> {
                    if (left > 0) {
                        resLeft = leftViewWidth
                    }
                    if (left < -rightViewWidth) {
                        resLeft = -rightViewWidth
                    }
                }
                rightView -> {
                    resLeft = max(left, contentViewWidth - rightViewWidth)
                }
                leftView -> {
                    if (left > leftViewWidth) {
                        resLeft = leftViewWidth
                    }
                }
            }
            return resLeft
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return max(leftViewWidth, rightViewWidth)
        }

        /**
         * view滑动后的回调
         * @param changedView
         * @param left
         * @param top
         * @param dx   x轴方向的改编值
         * @param dy
         */
        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            Log.i(TAG, "onViewPositionChanged-->dx=$dx")
            super.onViewPositionChanged(changedView, left, top, dx, dy)

            // 重新布局子view的位置
            when (changedView) {
                contentView -> {
                    rightView?.let { it.layout(it.left + dx, 0, it.right + dx, it.bottom) }
                    leftView?.let { it.layout(it.left + dx, 0, it.right + dx, it.bottom) }
                }
                leftView, rightView -> {
                    contentView?.let { it.layout(it.left + dx, 0, it.right + dx, it.bottom)  }
                }
            }
            // 状态发生改变
            when {
                contentView?.left == 0 && mStatus != SwipeStatus.Close -> {
                    mStatus = SwipeStatus.Close
                }
                contentView?.left == -rightViewWidth && mStatus != SwipeStatus.OpenRight -> {
                    mStatus = SwipeStatus.OpenRight
                }
                contentView?.left == leftViewWidth && mStatus != SwipeStatus.OpenLeft -> {
                    mStatus = SwipeStatus.OpenLeft
                }
                mStatus != SwipeStatus.Swiping -> {
                    mStatus = SwipeStatus.Swiping
                }
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)

            when {
                (contentView?.left ?: 0) < -rightViewWidth / 2 -> {
                    openRight()
                }
                (contentView?.left ?: 0) > leftViewWidth / 2 -> {
                    openLeft()
                }
                else -> {
                    close()
                }
            }
        }
    }

    init {
        mViewDragHelper = ViewDragHelper.create(this, dragCallback)

        val tpa = ctx.obtainStyledAttributes(attrs, R.styleable.SwipeView)
        mSwipeType = tpa.getInt(R.styleable.SwipeView_sv_swipeType, SWIPE_RIGHT)

        tpa.recycle()
    }

    private fun close() {
        mViewDragHelper.smoothSlideViewTo(contentView!!, 0, 0)
        ViewCompat.postInvalidateOnAnimation(this)
    }

    private fun openLeft() {
        mViewDragHelper.smoothSlideViewTo(contentView!!, leftViewWidth, 0)
        ViewCompat.postInvalidateOnAnimation(this)
    }

    private fun openRight() {
        mViewDragHelper.smoothSlideViewTo(contentView!!, -rightViewWidth, 0)
        ViewCompat.postInvalidateOnAnimation(this)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mViewDragHelper.continueSettling(true)) {   // 内部有Scroller计算位置和移动
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return mViewDragHelper.shouldInterceptTouchEvent(ev ?: return false)
    }

    private var lastX = 0F; private var lastY = 0F

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)
        val x = event.x; val y = event.y
        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                val dx = x - lastX; val dy = y - lastY
                if (abs(dx) > abs(dy) && (mStatus == SwipeStatus.Close || mStatus == SwipeStatus.Swiping)) {    // 想要横向滑，且当前状态为close
                    requestDisallowInterceptTouchEvent(true)
                }
            }
        }
        lastX = x
        lastY = y
        mViewDragHelper.processTouchEvent(event)
        return true
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        when (mSwipeType) {
            SWIPE_LEFT -> {
                if (childCount != 2) {
                    throw IllegalStateException("右滑模式，只支持两个子view，且第一个为左侧屏幕外的view")
                }
                leftView = getChildAt(0)
                contentView = getChildAt(1)
            }
            SWIPE_RIGHT -> {
                if (childCount != 2) {
                    throw IllegalStateException("左滑模式，只支持两个子view，且第二个为左侧屏幕外的view")
                }
                contentView = getChildAt(0)
                rightView = getChildAt(1)
            }
            SWIPE_BOTH -> {
                if (childCount != 3) {
                    throw IllegalStateException("左右滑都支持模式，只支持三个子view，且第一个为左侧屏幕外的view，第二个为内容，第三个为右侧屏幕外view")
                }
                leftView = getChildAt(0)
                contentView = getChildAt(1)
                rightView = getChildAt(2)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        contentViewWidth = contentView?.measuredWidth ?: 0
        contentViewHeight = contentView?.measuredHeight ?: 0
        leftViewWidth = leftView?.measuredWidth ?: 0
        leftViewHeight = leftView?.measuredHeight ?: 0
        rightViewWidth = rightView?.measuredWidth ?: 0
        rightViewHeight = rightView?.measuredHeight ?: 0
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        contentView?.layout(0, 0, contentViewWidth, contentViewHeight)
        rightView?.layout(contentViewWidth, 0, contentViewWidth + rightViewWidth, rightViewHeight)
        leftView?.layout(-leftViewWidth, 0, 0, leftViewHeight)
    }

    enum class SwipeStatus {
        OpenLeft,
        OpenRight,
        Close,
        Swiping
    }
}