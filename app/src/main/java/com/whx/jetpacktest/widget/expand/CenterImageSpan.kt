package com.whx.jetpacktest.widget.expand

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan


/**
 * 自定义ImageSpan 让Image 在行内居中显示
 */
class CenterImageSpan(private val drawable: Drawable, verticalAlignment: Int) : ImageSpan(
    drawable, verticalAlignment
) {
    override fun getDrawable(): Drawable {
        return drawable
    }

    override fun draw(
        canvas: Canvas, text: CharSequence,
        start: Int, end: Int, x: Float,
        top: Int, y: Int, bottom: Int, paint: Paint
    ) {
        // image to draw
        val b = getDrawable()
        // font metrics of text to be replaced
        val fm = paint.fontMetricsInt
        val transY = ((y + fm.descent + y + fm.ascent) / 2
                - b.bounds.bottom / 2)
        canvas.save()
        canvas.translate(x, transY.toFloat())
        b.draw(canvas)
        canvas.restore()
    }
}