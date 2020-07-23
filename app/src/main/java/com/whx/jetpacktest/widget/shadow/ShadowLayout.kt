package com.whx.jetpacktest.widget.shadow

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.whx.jetpacktest.R
import com.whx.jetpacktest.utils.dp
import kotlin.math.abs


class ShadowLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defArr: Int = 0) : FrameLayout(context, attrs, defArr) {
    private var mBackGroundColor = 0
    private var mBackGroundColorClicked = 0
    private var mShadowColor = 0
    private var mShadowRadius = 0f
    private var mCornerRadius = 0f
    private var mDx = 0f
    private var mDy = 0f
    private var leftShow = false
    private var rightShow = false
    private var topShow = false
    private var bottomShow = false
    private lateinit var shadowPaint: Paint
    private lateinit var paint: Paint

    private var leftPading = 0
    private var topPading = 0
    private var rightPading = 0
    private var bottomPading = 0
    //阴影布局子空间区域
    private val rectf = RectF()

    //ShadowLayout的样式，是只需要pressed还是selected,还是2者都需要，默认支持2者
    private var selectorType = 3
    private var isShowShadow = true
    private var isSym = false

    //增加各个圆角的属性
    private var mCornerRadiusLeftTop = 0f
    private var mCornerRadiusRightTop = 0f
    private var mCornerRadiusLeftBottom = 0f
    private var mCornerRadiusRightBottom = 0f

    init {
        initView(context, attrs)
    }

    private val DEFAULT_SHADOW_COLOR = Color.parseColor("#0D000000")
    private val DEFAULT_SHADOWBACK_COLOR = Color.WHITE


    //增加selector样式
    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (selectorType == 3 || selectorType == 2) {
            if (selected) {
                paint.color = mBackGroundColorClicked
            } else {
                paint.color = mBackGroundColor
            }
            postInvalidate()
        }
    }


    //动态设置x轴偏移量
    fun setShadowDx(mDx: Float) {
        if (abs(mDx) > mShadowRadius) {
            if (mDx > 0) {
                this.mDx = mShadowRadius
            } else {
                this.mDx = -mShadowRadius
            }
        } else {
            this.mDx = mDx
        }
        setPading()
    }

    //动态设置y轴偏移量
    fun setShadowDy(mDy: Float) {
        if (abs(mDy) > mShadowRadius) {
            if (mDy > 0) {
                this.mDy = mShadowRadius
            } else {
                this.mDy = -mShadowRadius
            }
        } else {
            this.mDy = mDy
        }
        setPading()
    }


    fun getCornerRadius(): Float {
        return mCornerRadius
    }

    //动态设置 圆角属性
    fun setCornerRadius(mCornerRadius: Int) {
        this.mCornerRadius = mCornerRadius.toFloat()
        if (width != 0 && height != 0) {
            setBackgroundCompat(width, height)
        }
    }

    fun getShadowLimit(): Float {
        return mShadowRadius
    }

    //动态设置阴影扩散区域
    fun setShadowLimit(mShadowLimit: Int) {
        this.mShadowRadius = mShadowLimit.toFloat()
        setPading()
    }

    //动态设置阴影颜色值
    fun setShadowColor(mShadowColor: Int) {
        this.mShadowColor = mShadowColor
        if (width != 0 && height != 0) {
            setBackgroundCompat(width, height)
        }
    }


    fun setLeftShow(leftShow: Boolean) {
        this.leftShow = leftShow
        setPading()
    }

    fun setRightShow(rightShow: Boolean) {
        this.rightShow = rightShow
        setPading()
    }

    fun setTopShow(topShow: Boolean) {
        this.topShow = topShow
        setPading()
    }

    fun setBottomShow(bottomShow: Boolean) {
        this.bottomShow = bottomShow
        setPading()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            setBackgroundCompat(w, h)
        }
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        initAttributes(context, attrs)
        shadowPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        //矩形画笔
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = mBackGroundColor
        }
        setPading()
    }


    fun setPading() { //控件区域是否对称，默认是对称。不对称的话，那么控件区域随着阴影区域走
        if (isSym) {
            val xPadding = (mShadowRadius + abs(mDx)).toInt()
            val yPadding = (mShadowRadius + abs(mDy)).toInt()
            leftPading = if (leftShow) {
                xPadding
            } else {
                0
            }
            topPading = if (topShow) {
                yPadding
            } else {
                0
            }
            rightPading = if (rightShow) {
                xPadding
            } else {
                0
            }
            bottomPading = if (bottomShow) {
                yPadding
            } else {
                0
            }
        } else {
            if (abs(mDy) > mShadowRadius) {
                mDy = if (mDy > 0) {
                    mShadowRadius
                } else {
                    0 - mShadowRadius
                }
            }
            if (abs(mDx) > mShadowRadius) {
                mDx = if (mDx > 0) {
                    mShadowRadius
                } else {
                    0 - mShadowRadius
                }
            }
            topPading = if (topShow) {
                (mShadowRadius - mDy).toInt()
            } else {
                0
            }
            bottomPading = if (bottomShow) {
                (mShadowRadius + mDy).toInt()
            } else {
                0
            }
            rightPading = if (rightShow) {
                (mShadowRadius - mDx).toInt()
            } else {
                0
            }
            leftPading = if (leftShow) {
                (mShadowRadius + mDx).toInt()
            } else {
                0
            }
        }
        setPadding(leftPading, topPading, rightPading, bottomPading)
    }


    private fun setBackgroundCompat(w: Int, h: Int) {
        if (isShowShadow) { //判断传入的颜色值是否有透明度
            isAddAlpha(mShadowColor)
            val bitmap = createShadowBitmap(
                w,
                h,
                mCornerRadius,
                mShadowRadius,
                mDx,
                mDy,
                mShadowColor,
                Color.TRANSPARENT
            )
            val drawable = BitmapDrawable(resources, bitmap)
            background = drawable
        } else { //解决不执行onDraw方法的bug就是给其设置一个透明色
            setBackgroundColor(Color.parseColor("#00000000"))
        }
    }


    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout) ?: return
        try { //默认是显示
            isShowShadow = attr.getBoolean(R.styleable.ShadowLayout_isShowShadow, true)
            leftShow = attr.getBoolean(R.styleable.ShadowLayout_leftShow, true)
            rightShow = attr.getBoolean(R.styleable.ShadowLayout_rightShow, true)
            bottomShow = attr.getBoolean(R.styleable.ShadowLayout_bottomShow, true)
            topShow = attr.getBoolean(R.styleable.ShadowLayout_topShow, true)
            mCornerRadius = attr.getDimension(R.styleable.ShadowLayout_cornerRadius, 0f)
            mCornerRadiusLeftTop =
                attr.getDimension(R.styleable.ShadowLayout_cornerRadius_leftTop, -1f)
            mCornerRadiusLeftBottom =
                attr.getDimension(R.styleable.ShadowLayout_cornerRadius_leftBottom, -1f)
            mCornerRadiusRightTop =
                attr.getDimension(R.styleable.ShadowLayout_cornerRadius_rigthTop, -1f)
            mCornerRadiusRightBottom =
                attr.getDimension(R.styleable.ShadowLayout_cornerRadius_rightBottom, -1f)
            //默认扩散区域宽度
            mShadowRadius = attr.getDimension(R.styleable.ShadowLayout_shadow_radius, 5.dp())
            //x轴偏移量
            mDx = attr.getDimension(R.styleable.ShadowLayout_shadow_dx, 0f)
            //y轴偏移量
            mDy = attr.getDimension(R.styleable.ShadowLayout_shadow_dy, 0f)
            mShadowColor = attr.getColor(R.styleable.ShadowLayout_shadowColor, DEFAULT_SHADOW_COLOR)
            mBackGroundColor = attr.getColor(R.styleable.ShadowLayout_shadowBackColor, DEFAULT_SHADOWBACK_COLOR)
            mBackGroundColorClicked = attr.getColor(R.styleable.ShadowLayout_shadowBackColorClicked, DEFAULT_SHADOW_COLOR)
            if (mBackGroundColorClicked != -1) {
                isClickable = true
            }
            selectorType = attr.getInt(R.styleable.ShadowLayout_selectorMode, 3)
            isSym = attr.getBoolean(R.styleable.ShadowLayout_isSym, true)
        } finally {
            attr.recycle()
        }
    }


    private fun createShadowBitmap(
        shadowWidth: Int, shadowHeight: Int, cornerRadius: Float, shadowRadius: Float,
        dx: Float, dy: Float, shadowColor: Int, fillColor: Int): Bitmap { //优化阴影bitmap大小,将尺寸缩小至原来的1/4。
        val sWidth = shadowWidth / 4
        val sHeight = shadowHeight / 4
        val cRadius = cornerRadius / 4
        val sRadius = shadowRadius / 4
        val dx1 = dx / 4
        val dy1 = dy / 4

        val output = Bitmap.createBitmap(sWidth, sHeight, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(output)
        //这里缩小limt的是因为，setShadowLayer后会将bitmap扩散到shadowWidth，shadowHeight
        val shadowRect = RectF(sRadius, sRadius, sWidth - sRadius, sHeight - sRadius)

        if (isSym) {
            if (dy1 > 0) {
                shadowRect.top += dy1
                shadowRect.bottom -= dy1
            } else if (dy1 < 0) {
                shadowRect.top += abs(dy1)
                shadowRect.bottom -= abs(dy1)
            }
            if (dx1 > 0) {
                shadowRect.left += dx1
                shadowRect.right -= dx1
            } else if (dx1 < 0) {
                shadowRect.left += abs(dx1)
                shadowRect.right -= abs(dx1)
            }
        } else {
            shadowRect.top -= dy1
            shadowRect.bottom -= dy1
            shadowRect.right -= dx1
            shadowRect.left -= dx1
        }
        shadowPaint.color = fillColor
        if (!isInEditMode) { //dx  dy
            shadowPaint.setShadowLayer(sRadius, dx1, dy1, shadowColor)
        }
        if (mCornerRadiusLeftBottom == -1f && mCornerRadiusLeftTop == -1f && mCornerRadiusRightTop == -1f && mCornerRadiusRightBottom == -1f) { //如果没有设置整个属性，那么按原始去画
            canvas.drawRoundRect(shadowRect, cRadius, cRadius, shadowPaint)
        } else { //目前最佳的解决方案
            rectf.left = leftPading.toFloat()
            rectf.top = topPading.toFloat()
            rectf.right = width - rightPading.toFloat()
            rectf.bottom = height - bottomPading.toFloat()
            val trueHeight: Int
            val heightLength = height - bottomPading - topPading
            val widthLength = width - rightPading - leftPading
            trueHeight = if (widthLength > heightLength) {
                heightLength
            } else {
                widthLength
            }
            val rate = 0.62f //0.56
            //只要设置一个后就先按照全部圆角设置
            canvas.drawRoundRect(shadowRect, trueHeight.toFloat() / 2, trueHeight.toFloat() / 2, shadowPaint)
            if (mCornerRadiusLeftTop != -1f) {
                val rateLeftTop = mCornerRadiusLeftTop / (trueHeight / 2)
                if (rateLeftTop <= rate) {
                    canvas.drawRoundRect(
                        RectF(
                            shadowRect.left,
                            shadowRect.top,
                            shadowRect.left + trueHeight / 8,
                            shadowRect.top + trueHeight / 8
                        ), mCornerRadiusLeftTop / 4, mCornerRadiusLeftTop / 4, shadowPaint
                    )
                }
            } else {
                val rateSrc = mCornerRadius / (trueHeight / 2)
                if (rateSrc <= rate) {
                    canvas.drawRoundRect(
                        RectF(
                            shadowRect.left,
                            shadowRect.top,
                            shadowRect.left + trueHeight / 8,
                            shadowRect.top + trueHeight / 8
                        ), mCornerRadius / 4, mCornerRadius / 4, shadowPaint
                    )
                }
            }
            if (mCornerRadiusLeftBottom != -1f) {
                val rateLeftBottom = mCornerRadiusLeftBottom / (trueHeight / 2)
                if (rateLeftBottom <= rate) {
                    canvas.drawRoundRect(
                        RectF(
                            shadowRect.left,
                            shadowRect.bottom - trueHeight / 8,
                            shadowRect.left + trueHeight / 8,
                            shadowRect.bottom
                        ), mCornerRadiusLeftBottom / 4, mCornerRadiusLeftBottom / 4, shadowPaint
                    )
                }
            } else {
                val rateSrc = mCornerRadius / (trueHeight / 2)
                if (rateSrc <= rate) {
                    canvas.drawRoundRect(
                        RectF(
                            shadowRect.left,
                            shadowRect.bottom - trueHeight / 8,
                            shadowRect.left + trueHeight / 8,
                            shadowRect.bottom
                        ), mCornerRadius / 4, mCornerRadius / 4, shadowPaint
                    )
                }
            }
            if (mCornerRadiusRightTop != -1f) {
                val rateRightTop = mCornerRadiusRightTop / (trueHeight / 2)
                if (rateRightTop <= rate) {
                    canvas.drawRoundRect(
                        RectF(
                            shadowRect.right - trueHeight / 8,
                            shadowRect.top,
                            shadowRect.right,
                            shadowRect.top + trueHeight / 8
                        ), mCornerRadiusRightTop / 4, mCornerRadiusRightTop / 4, shadowPaint
                    )
                }
            } else {
                val rateSrc = mCornerRadius / (trueHeight / 2)
                if (rateSrc <= rate) {
                    canvas.drawRoundRect(
                        RectF(
                            shadowRect.right - trueHeight / 8,
                            shadowRect.top,
                            shadowRect.right,
                            shadowRect.top + trueHeight / 8
                        ), mCornerRadius / 4, mCornerRadius / 4, shadowPaint
                    )
                }
            }
            if (mCornerRadiusRightBottom != -1f) {
                val rateRightBottom = mCornerRadiusRightBottom / (trueHeight / 2)
                if (rateRightBottom <= rate) {
                    canvas.drawRoundRect(
                        RectF(
                            shadowRect.right - trueHeight / 8,
                            shadowRect.bottom - trueHeight / 8,
                            shadowRect.right,
                            shadowRect.bottom
                        ), mCornerRadiusRightBottom / 4, mCornerRadiusRightBottom / 4, shadowPaint
                    )
                }
            } else {
                val rateSrc = mCornerRadius / (trueHeight / 2)
                if (rateSrc <= rate) {
                    canvas.drawRoundRect(
                        RectF(
                            shadowRect.right - trueHeight / 8,
                            shadowRect.bottom - trueHeight / 8,
                            shadowRect.right,
                            shadowRect.bottom
                        ), mCornerRadius / 4, mCornerRadius / 4, shadowPaint
                    )
                }
            }
        }
        return output
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rectf.left = leftPading.toFloat()
        rectf.top = topPading.toFloat()
        rectf.right = width - rightPading.toFloat()
        rectf.bottom = height - bottomPading.toFloat()
        val trueHeight = (rectf.bottom - rectf.top).toInt()
        //如果都为0说明，没有设置特定角，那么按正常绘制
        if (mCornerRadiusLeftTop == 0f && mCornerRadiusLeftBottom == 0f && mCornerRadiusRightTop == 0f && mCornerRadiusRightBottom == 0f) {
            if (mCornerRadius > trueHeight / 2) { //画圆角矩形
                canvas.drawRoundRect(rectf, trueHeight.toFloat() / 2, trueHeight.toFloat() / 2, paint)
            } else {
                canvas.drawRoundRect(rectf, mCornerRadius, mCornerRadius, paint)
            }
        } else {
            setSpaceCorner(canvas, trueHeight)
        }
    }


    //这是自定义四个角的方法。
    private fun setSpaceCorner(canvas: Canvas, trueHeight: Int) {
        var leftTop: Int
        var rightTop: Int
        var rightBottom: Int
        var leftBottom: Int
        leftTop = if (mCornerRadiusLeftTop == -1f) {
            mCornerRadius.toInt()
        } else {
            mCornerRadiusLeftTop.toInt()
        }
        if (leftTop > trueHeight / 2) {
            leftTop = trueHeight / 2
        }
        rightTop = if (mCornerRadiusRightTop == -1f) {
            mCornerRadius.toInt()
        } else {
            mCornerRadiusRightTop.toInt()
        }
        if (rightTop > trueHeight / 2) {
            rightTop = trueHeight / 2
        }
        rightBottom = if (mCornerRadiusRightBottom == -1f) {
            mCornerRadius.toInt()
        } else {
            mCornerRadiusRightBottom.toInt()
        }
        if (rightBottom > trueHeight / 2) {
            rightBottom = trueHeight / 2
        }
        leftBottom = if (mCornerRadiusLeftBottom == -1f) {
            mCornerRadius.toInt()
        } else {
            mCornerRadiusLeftBottom.toInt()
        }
        if (leftBottom > trueHeight / 2) {
            leftBottom = trueHeight / 2
        }
        val outerR = floatArrayOf(
            leftTop.toFloat(),
            leftTop.toFloat(),
            rightTop.toFloat(),
            rightTop.toFloat(),
            rightBottom.toFloat(),
            rightBottom.toFloat(),
            leftBottom.toFloat(),
            leftBottom.toFloat()
        ) //左上，右上，右下，左下
        val mDrawables = ShapeDrawable(RoundRectShape(outerR, null, null))
        mDrawables.paint.color = paint.color
        mDrawables.setBounds(
            leftPading,
            topPading,
            width - rightPading,
            height - bottomPading
        )
        mDrawables.draw(canvas)
    }


    fun isAddAlpha(color: Int) { //获取单签颜色值的透明度，如果没有设置透明度，默认加上#2a
        if (Color.alpha(color) == 255) {
            var red = Integer.toHexString(Color.red(color))
            var green = Integer.toHexString(Color.green(color))
            var blue = Integer.toHexString(Color.blue(color))
            if (red.length == 1) {
                red = "0$red"
            }
            if (green.length == 1) {
                green = "0$green"
            }
            if (blue.length == 1) {
                blue = "0$blue"
            }
            val endColor = "#2a$red$green$blue"
            mShadowColor = convertToColorInt(endColor)
        }
    }


    @Throws(IllegalArgumentException::class)
    fun convertToColorInt(argb: String): Int {
        var argb1 = argb
        if (!argb1.startsWith("#")) {
            argb1 = "#$argb"
        }
        return Color.parseColor(argb1)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mBackGroundColorClicked != -1) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> if (!this@ShadowLayout.isSelected && selectorType != 2) {
                    paint.color = mBackGroundColorClicked
                    postInvalidate()
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> if (!this@ShadowLayout.isSelected && selectorType != 2) {
                    paint.color = mBackGroundColor
                    postInvalidate()
                }
            }
        }
        return super.onTouchEvent(event)
    }
}