package com.whx.jetpacktest.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.whx.jetpacktest.NBApplication

object CommonUtils {

    fun getScreenHeight(): Int {
        val wm = NBApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getRealMetrics(dm)

        return dm.heightPixels
    }
}