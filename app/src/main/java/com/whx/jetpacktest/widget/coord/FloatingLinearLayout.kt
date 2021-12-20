package com.whx.jetpacktest.widget.coord

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import com.whx.jetpacktest.R
import java.util.*

/**
 * 支持在本layout 内设置吸顶child，可给任意child 设置吸顶
 * todo 慢速滑动时吸顶view 抖动
 */
class FloatingLinearLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defS: Int = 0) :
    LinearLayout(context, attrs, defS) {
    private var mCurrentOffset = 0
    private var mOffsetChangeListener: AppBarLayout.OnOffsetChangedListener? = null

    init {
        isChildrenDrawingOrderEnabled = true
    }

    private val mPinChildren = ArrayList<View>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var minH = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams
            if (lp is CustomLayoutParams && lp.pin) {
                minH += child.measuredHeight
            }
        }
        minimumHeight = minH
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams
            if (lp is CustomLayoutParams && lp.pin) {
                lp.originTop = child.top
                lp.originBottom = child.bottom
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams
            if (lp is CustomLayoutParams && lp.pin) {
                mPinChildren.add(child)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val parent = parent
        if (parent is AppBarLayout) {
            ViewCompat.setFitsSystemWindows(this, ViewCompat.getFitsSystemWindows(parent))

            if (mOffsetChangeListener == null) {
                mOffsetChangeListener = OffsetUpdateListener()
            }
            parent.addOnOffsetChangedListener(mOffsetChangeListener)

            ViewCompat.requestApplyInsets(this)
        }
    }

    override fun onDetachedFromWindow() {
        val parent = parent
        if (mOffsetChangeListener != null && parent is AppBarLayout) {
            parent.removeOnOffsetChangedListener(mOffsetChangeListener)
        }
        super.onDetachedFromWindow()
    }

    override fun getChildDrawingOrder(childCount: Int, drawingPosition: Int): Int {
        return childCount - drawingPosition - 1
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return CustomLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return CustomLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): LayoutParams {
        return CustomLayoutParams(lp)
    }

    class CustomLayoutParams : LayoutParams {
        var pin = false
        var originTop = 0
        var originBottom = 0

        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
            val tpa = context.obtainStyledAttributes(attrs, R.styleable.FloatingLinearLayout_Layout)
            pin = tpa.getBoolean(R.styleable.FloatingLinearLayout_Layout_fll_layout_pin, false)
            tpa.recycle()
        }

        constructor(width: Int, height: Int) : super(width, height)
        constructor(width: Int, height: Int, weight: Float) : super(width, height, weight)
        constructor(params: ViewGroup.LayoutParams?) : super(params)
        constructor(source: MarginLayoutParams) : super(source)
        constructor(source: LayoutParams) : super(source)
    }

    private inner class OffsetUpdateListener : AppBarLayout.OnOffsetChangedListener {
        private var preLayoutOffset = Int.MIN_VALUE

        override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
            if (mCurrentOffset != verticalOffset) {
                mCurrentOffset = verticalOffset


                val layoutOffset = -mCurrentOffset - top
                val up = layoutOffset - preLayoutOffset > 0

                for (i in mPinChildren.indices) {
                    val child = mPinChildren[i]
                    val lp = child.layoutParams as CustomLayoutParams

                    var offset = 0

                    if (i == 0) {
                        if (layoutOffset < lp.originTop) {
                            offset = 0
                            if (!up && child.top > lp.originTop) {      // 防止快速下滑时view 不能复位
                                offset = lp.originTop - child.top
                            }
                            Log.e("-------", "i == 0, layoutOffset < lp.originTop, child top: ${child.top}, origin top: ${lp.originTop}")
                        } else {
                            offset = layoutOffset - child.top

                            Log.e("-------", "i == 0, layoutOffset >= lp.originTop, layout offset: $layoutOffset, child top: ${child.top}")
                        }

                    } else {
                        val preHeight = getPrePinViewHeight(i)
                        if (lp.originTop - layoutOffset > preHeight) {
                            offset = 0
                            if (!up && child.top > lp.originTop) {      // 防止快速下滑时view 不能复位
                                offset = lp.originTop - child.top
                            }
                            Log.e("-------", "i == $i, lp.originTop - layoutOffset > preHeight, child top: ${child.top}, origin top: ${lp.originTop}")
                        } else {
                            offset = layoutOffset + preHeight - child.top
                            Log.e(
                                "-------",
                                "i == $i, lp.originTop - layoutOffset <= preHeight, layout offset: ${layoutOffset + preHeight}, child top: ${child.top}"
                            )
                        }

                    }
                    Log.e("---------", "view: ${child.contentDescription}, offset: $offset")

                    getOffsetHelper(child).setTopAndBottomOffset(offset)
                }
                preLayoutOffset = layoutOffset
            }
        }
    }

    private fun getPrePinViewHeight(pos: Int): Int {
        var h = 0
        for (i in 0 until pos) {
            h += mPinChildren[i].height
        }
        return h
    }

    private fun getOffsetHelper(view: View): ViewOffsetHelper {
        var offsetHelper = view.getTag(com.google.android.material.R.id.view_offset_helper) as? ViewOffsetHelper
        if (offsetHelper == null) {
            offsetHelper = ViewOffsetHelper(view)
            view.setTag(com.google.android.material.R.id.view_offset_helper, offsetHelper)
        }
        return offsetHelper
    }
}