package com.whx.jetpacktest.widget.coord.behavior

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import androidx.palette.graphics.Palette
import com.whx.jetpacktest.R
import com.whx.jetpacktest.utils.StatusBarUtil
import com.whx.jetpacktest.utils.dp
import kotlin.math.roundToInt

class FaceBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {
    constructor() : this(null, null)

    private val topBarHeight = 56.dp() + StatusBarUtil.getStatusBarHeight(context)
    private val contentTransY = 280.dp()
    private val downEndY = 360.dp()
    private val faceTransY = (-12).dp()

    private val mMaskDrawable: GradientDrawable

    init {
        val palette = Palette.from(BitmapFactory.decodeResource(context?.resources, R.mipmap.header)).generate()
        val vibrantSwatch = palette.vibrantSwatch
        val mutedSwatch = palette.mutedSwatch

        val colors = IntArray(2)
        when {
            mutedSwatch != null -> {
                colors[0] = mutedSwatch.rgb
                colors[1] = getTranslucentColor(0.6f, mutedSwatch.rgb)
            }
            vibrantSwatch != null -> {
                colors[0] = vibrantSwatch.rgb
                colors[1] = getTranslucentColor(0.6f, vibrantSwatch.rgb)
            }
            else -> {
                colors[0] = Color.parseColor("$4D000000")
                colors[1] = getTranslucentColor(0.6f, Color.parseColor("$4D000000"))
            }
        }
        mMaskDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency.id == R.id.ll_content
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: View, layoutDirection: Int): Boolean {
        child.findViewById<View>(R.id.mask_v).background = mMaskDrawable
        return false
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        val upPro = (contentTransY - MathUtils.clamp(
            dependency.translationY,
            topBarHeight,
            contentTransY
        )) / (contentTransY - topBarHeight)
        val downPro =
            (downEndY - MathUtils.clamp(dependency.translationY, contentTransY, downEndY)) / (downEndY - contentTransY)

        val imageView = child.findViewById<ImageView>(R.id.face_iv)
        val mask = child.findViewById<View>(R.id.mask_v)

        if (dependency.translationY >= contentTransY) {
            imageView.translationY = downPro * faceTransY
        } else {
            imageView.translationY = faceTransY + 4 * upPro * faceTransY
        }
        imageView.alpha = 1 - upPro
        mask.alpha = upPro
        return true
    }

    private fun getTranslucentColor(percent: Float, rgb: Int): Int {
        val blue = Color.blue(rgb)
        val green = Color.green(rgb)
        val red = Color.red(rgb)
        var alpha = Color.alpha(rgb)
        alpha = (alpha * percent).roundToInt()
        return Color.argb(alpha, red, green, blue)
    }
}