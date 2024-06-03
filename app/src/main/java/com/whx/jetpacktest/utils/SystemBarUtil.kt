package com.whx.jetpacktest.utils

import android.content.res.Resources
import android.view.View
import android.view.Window
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

object SystemBarUtil {
    private fun getNavBarHeight(): Int {
        val res = Resources.getSystem()
        val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId != 0) {
            res.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    fun getNavigationBarHeight(window: Window, callback: NavigationBarCallback) {

        val view = window.decorView
        val attachedToWindow = view.isAttachedToWindow

        if (attachedToWindow) {
            val windowInsets = ViewCompat.getRootWindowInsets(view) ?: return

            val height = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

            val hasNavigationBar = windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars()) &&
                    windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom > 0

            if (height > 0) {
                callback.onHeight(height, hasNavigationBar)
            } else {
                callback.onHeight(getNavBarHeight(), hasNavigationBar)
            }

        } else {
            view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {

                override fun onViewAttachedToWindow(v: View) {

                    val windowInsets = ViewCompat.getRootWindowInsets(v) ?: return

                    val height = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

                    val hasNavigationBar = windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars()) &&
                            windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom > 0

                    if (height > 0) {
                        callback.onHeight(height, hasNavigationBar)
                    } else {
                        callback.onHeight(getNavBarHeight(), hasNavigationBar)
                    }

                }

                override fun onViewDetachedFromWindow(v: View) {
                }
            })
        }
    }

    interface NavigationBarCallback {
        fun onHeight(height: Int, hasNav: Boolean)
    }
}