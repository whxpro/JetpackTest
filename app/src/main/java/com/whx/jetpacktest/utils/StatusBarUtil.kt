package com.whx.jetpacktest.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import com.whx.jetpacktest.NBApplication

object StatusBarUtil {
    private const val STATUS_BAR_VIEW_TAG = "status_bar_view"

    fun setStatusBarColor(window: Window, color: Int) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                setStatusBarColorM(window, color)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                setStatusBarColorL(window, color)
            }
            else -> {
                setStatusBarColorK(window, color)
            }
        }
    }

    fun getStatusBarHeight(context: Context?): Int {
        if (context is Activity) {
            val rect = Rect()
            context.window.decorView.getWindowVisibleDisplayFrame(rect)
            return rect.top
        }
        val resId = NBApplication.getAppContext().resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            return NBApplication.getAppContext().resources.getDimensionPixelSize(resId)
        }
        return 0
    }

    private fun setStatusBarColorK(window: Window, @ColorInt color: Int) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        val decorViewGroup = window.decorView as ViewGroup

        var statusBarView = decorViewGroup.findViewWithTag<StatusBarView>(STATUS_BAR_VIEW_TAG)
        if (statusBarView == null) {
            statusBarView = StatusBarView(window.context)
            statusBarView.tag = STATUS_BAR_VIEW_TAG
            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.TOP
            statusBarView.layoutParams = params
            decorViewGroup.addView(statusBarView)
        }
        statusBarView.setBackgroundColor(color)
//        StatusBarCompat.internalSetFitsSystemWindows(window, true)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setStatusBarColorL(window: Window, @ColorInt color: Int) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setStatusBarColorM(window: Window, @ColorInt color: Int) {
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //设置状态栏颜色
        window.statusBarColor = color

        // 去掉系统状态栏下的windowContentOverlay
        val v = window.findViewById<View>(android.R.id.content)
        if (v != null) {
            v.foreground = null
        }
    }
}
class StatusBarView(context: Context, attrs: AttributeSet? = null, defS: Int = 0) : View(context, attrs, defS) {

}