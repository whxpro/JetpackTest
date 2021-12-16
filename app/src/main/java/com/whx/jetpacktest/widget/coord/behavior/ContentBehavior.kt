package com.whx.jetpacktest.widget.coord.behavior

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.whx.jetpacktest.R
import com.whx.jetpacktest.utils.StatusBarUtil
import com.whx.jetpacktest.utils.dp
import java.lang.Exception

class ContentBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {
    constructor() : this(null, null)

    private val topBarHeight = 56.dp() + StatusBarUtil.getStatusBarHeight(context)
    private val contentTransY = 280.dp()
    private val downEndY = 360.dp()

    private var mContentView: View? = null

    private val mRestoreAnim = ValueAnimator().apply {
        addUpdateListener {
            translation(mContentView, it.animatedValue as Float)
        }
    }

    private var flingFromCollaps = false

    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: View,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {
        val childLpH = child.layoutParams.height
        if (childLpH == ViewGroup.LayoutParams.MATCH_PARENT || childLpH == ViewGroup.LayoutParams.WRAP_CONTENT) {
            var aH = View.MeasureSpec.getSize(parentHeightMeasureSpec)
            if (aH == 0) {
                aH = parent.height
            }
            val h = aH - topBarHeight
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(h.toInt(), if (childLpH == ViewGroup.LayoutParams.MATCH_PARENT) View.MeasureSpec.EXACTLY else View.MeasureSpec.AT_MOST)
            parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, hMeasureSpec, heightUsed)

            return true
        }
        return false
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: View, layoutDirection: Int): Boolean {
        val handleLayout = super.onLayoutChild(parent, child, layoutDirection)
        mContentView = child
        return handleLayout
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return directTargetChild.id == R.id.ll_content && axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScrollAccepted(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ) {
        if (mRestoreAnim.isStarted) {
            mRestoreAnim.cancel()
        }
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, type: Int) {
        if (child.translationY > contentTransY) {
            restore()
        }
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        val transY = child.translationY - dy

        if (dy > 0) {
            if (transY >= topBarHeight) {
                translationByConsume(child, transY, consumed, dy)
            } else {
                translationByConsume(child, topBarHeight, consumed, (child.translationY - topBarHeight).toInt())
            }
        }

        if (dy < 0 && !target.canScrollVertically(-1)) {
            if (type == ViewCompat.TYPE_NON_TOUCH && transY >= contentTransY && flingFromCollaps) {
                flingFromCollaps = false
                translationByConsume(child, contentTransY, consumed, dy)
                stopViewScroll(target)
                return
            }

            if (transY in topBarHeight..downEndY) {
                translationByConsume(child, transY, consumed, dy)
            } else {
                translationByConsume(child, downEndY, consumed, (downEndY - child.translationY).toInt())
                stopViewScroll(target)
            }
        }
    }

    private fun stopViewScroll(target: View) {
        if (target is RecyclerView) {
            target.stopScroll()
        }
        if (target is NestedScrollView) {
            try {
                val clazz = target.javaClass
                val mScroller = clazz.getDeclaredField("mScroller")
                mScroller.isAccessible = true
                val overScroller = mScroller.get(target) as OverScroller
                overScroller.abortAnimation()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun translationByConsume(view: View, transY: Float, consumed: IntArray, consumeDy: Int) {
        consumed[1] = consumeDy
        view.translationY = transY
    }

    private fun restore() {
        if (mRestoreAnim.isStarted) {
            mRestoreAnim.cancel()
            mRestoreAnim.removeAllListeners()
        }
        mRestoreAnim.setFloatValues(mContentView?.translationY ?: 0f, contentTransY)
        mRestoreAnim.duration = 300
        mRestoreAnim.start()
    }

    private fun translation(view: View?, transY: Float) {
        view?.translationY = transY
    }

    override fun onDetachedFromLayoutParams() {
        if (mRestoreAnim.isStarted) {
            mRestoreAnim.run {
                cancel()
                removeAllUpdateListeners()
                removeAllListeners()
            }
        }
        super.onDetachedFromLayoutParams()
    }
}