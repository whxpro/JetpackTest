package com.whx.jetpacktest.widget.coord.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import com.whx.jetpacktest.R
import com.whx.jetpacktest.utils.StatusBarUtil
import com.whx.jetpacktest.utils.dp

class TopBarBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {
    constructor() : this(null, null)

    private val topBarHeight = 56.dp() + StatusBarUtil.getStatusBarHeight(context)
    private val contentTransY = 280.dp()

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency.id == R.id.ll_content
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        val upPro = (contentTransY - MathUtils.clamp(dependency.translationY, topBarHeight, contentTransY)) / (contentTransY - topBarHeight)
        val nameTv = child.findViewById<View>(R.id.title_tv)
        val followTv = child.findViewById<View>(R.id.follow_tv)
        nameTv.alpha = upPro
        followTv.alpha = upPro
        return true
    }
}