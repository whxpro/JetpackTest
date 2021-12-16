package com.whx.jetpacktest.widget.coord.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import com.whx.jetpacktest.R
import com.whx.jetpacktest.utils.StatusBarUtil
import com.whx.jetpacktest.utils.dp

class TitleBarBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {
    constructor() : this(null, null)

    private val topBarHeight = 56.dp() + StatusBarUtil.getStatusBarHeight(context)
    private val contentTransY = 280.dp()

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency.id == R.id.ll_content
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        adjustPosition(parent, child, dependency)
        val start = (contentTransY + topBarHeight) / 2
        val upPro = (contentTransY - MathUtils.clamp(dependency.translationY, start, contentTransY)) / (contentTransY - start)
        child.alpha = 1 - upPro
        return true
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: View, layoutDirection: Int): Boolean {
        val dependencies = parent.getDependencies(child)
        var dep: View? = null
        for (v in dependencies) {
            if (v.id == R.id.ll_content) {
                dep = v
                break
            }
        }
        if (dep != null) {
            adjustPosition(parent, child, dep)
            return true
        }
        return false
    }

    private fun adjustPosition(parent: CoordinatorLayout, child: View, dependency: View) {
        val lp = child.layoutParams as CoordinatorLayout.LayoutParams
        val left = parent.paddingLeft + lp.leftMargin
        val top = (dependency.y - child.measuredHeight + lp.topMargin).toInt()
        val right = child.measuredWidth + left - parent.paddingRight - lp.rightMargin
        val bottom = (dependency.y - lp.bottomMargin).toInt()
        child.layout(left, top, right, bottom)
    }
}