package com.whx.jetpacktest.utils

import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.view.WindowInsetsCompat
import kotlin.math.abs
import kotlin.math.max

object KeyboardUtil {

    private var sDecorViewInvisibleHeightPre = 0
    private var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null
    private var mNavHeight = 0

    private var sDecorViewDelta = 0


    private fun getDecorViewInvisibleHeight(window: Window): Int {
        val decorView = window.decorView

        val outRect = Rect()
        decorView.getWindowVisibleDisplayFrame(outRect)

        val delta = abs(decorView.bottom - outRect.bottom)
        if (delta <= mNavHeight) {
            sDecorViewDelta = delta
            return 0
        }
        return delta - sDecorViewDelta
    }

    fun registerKeyboardHeightListener(window: Window?, listener: KeyboardHeightListener) {
        window ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            invokeAbove31(window, listener)
        } else {
            invokeBelow31(window, listener)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private fun invokeAbove31(window: Window, listener: KeyboardHeightListener) {

        window.decorView
            .setWindowInsetsAnimationCallback(object : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {

                override fun onProgress(windowInsets: WindowInsets, list: List<WindowInsetsAnimation>): WindowInsets {

                    val imeHeight = windowInsets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                    val navHeight = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                    val hasNavigationBar = windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars()) &&
                            windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom > 0

                    listener.onKeyboardHeightChanged(
                        if (hasNavigationBar) max(
                            imeHeight - navHeight,
                            0
                        ) else imeHeight
                    )

                    return windowInsets
                }
            })
    }

    private fun invokeBelow31(window: Window, listener: KeyboardHeightListener) {
        val flags = window.attributes.flags

        if ((flags and WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) != 0) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        val contentView = window.findViewById<View>(android.R.id.content)
        sDecorViewInvisibleHeightPre = getDecorViewInvisibleHeight(window)

        onGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {

            override fun onGlobalLayout() {
                val height = getDecorViewInvisibleHeight(window)
                if (sDecorViewInvisibleHeightPre != height) {

                    listener.onKeyboardHeightChanged(height)

                    sDecorViewInvisibleHeightPre = height
                }
            }
        }

        //获取到导航栏高度之后再添加布局监听
        SystemBarUtil.getNavigationBarHeight(window, object : SystemBarUtil.NavigationBarCallback {
            override fun onHeight(height: Int, hasNav: Boolean) {
                mNavHeight = height
                contentView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener)
            }
        })

    }

    fun unregisterKeyboardHeightListener(window: Window?) {
        window ?: return
        val contentView = window.decorView.findViewById<View>(android.R.id.content)
        contentView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener)

        onGlobalLayoutListener = null
    }

    interface KeyboardHeightListener {
        fun onKeyboardHeightChanged(height: Int)
    }
}