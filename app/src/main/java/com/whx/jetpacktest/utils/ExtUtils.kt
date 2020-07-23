package com.whx.jetpacktest.utils

import android.widget.Toast
import com.whx.jetpacktest.NBApplication

fun Number.dp(): Float {
    return dp2px(this.toFloat())
}

fun dp2px(dp: Float): Float {
    val scale = NBApplication.getAppContext().resources.displayMetrics.density
    return dp * scale + 0.5f
}

fun toast(msg: String) {
    Toast.makeText(NBApplication.getAppContext(), msg, Toast.LENGTH_SHORT).show()
}