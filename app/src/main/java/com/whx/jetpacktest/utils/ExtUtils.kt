package com.whx.jetpacktest.utils

import android.widget.Toast
import com.whx.jetpacktest.NBApplication

private val mToast = Toast.makeText(NBApplication.getAppContext(), "", Toast.LENGTH_SHORT)

fun Number.dp(): Float {
    return dp2px(this.toFloat())
}

fun dp2px(dp: Float): Float {
    val scale = NBApplication.getAppContext().resources.displayMetrics.density
    return dp * scale + 0.5f
}

fun toast(msg: CharSequence) {
    mToast.setText(msg)
    mToast.show()
}