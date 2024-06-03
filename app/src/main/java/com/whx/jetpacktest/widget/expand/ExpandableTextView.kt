package com.whx.jetpacktest.widget.expand

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.DynamicLayout
import android.text.Layout
import android.text.Selection
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.method.Touch
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.whx.jetpacktest.R
import com.whx.jetpacktest.widget.expand.parser.IParser


import java.util.Locale
import java.util.PriorityQueue

/**
 * 一个支持展开 收起 网页链接 和 @用户 点击识别 的TextView
 */
@OptIn(ExperimentalStdlibApi::class)
class ExpandableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "ExpandableTextView"

        private const val DEF_MAX_LINE = 3
        private var TEXT_FOLD: String = "Collapse"
        private var TEXT_EXPEND: String = "More"
        private const val SPACE: String = " "

        private const val DEFAULT_CONTENT: String = ""

        private var retryTime = 0
    }

    private var linkHit = false

    /**
     * 记录当前的状态
     */
    private var mStatus = StatusType.STATUS_FOLD

    private lateinit var mFormatData: FormatData

    /**
     * 计算的layout
     */
    private var mDynamicLayout: DynamicLayout? = null

    // hide状态下，展示多少行开始省略
    private var mMaxLines = Int.MAX_VALUE

    private var currentLines = 0

    private var mWidth = 0

    private var mMoreArrow: Drawable? = null

    /**
     * 展开或者收回事件监听
     */
    private var expandOrContractClickListener: OnExpandOrFoldClickListener? = null

    /**
     * 点击展开或者收回按钮的时候 是否真的执行操作
     */
    private var needRealExpandOrFold = true

    /**
     * 是否需要收起
     */
    private var mNeedFold = true

    /**
     * 是否需要展开功能
     */
    private var mNeedExpend = true

    /**
     * 是否需要永远将展开或收回显示在最右边
     */
    private var mNeedAlwaysShowRight = false

    /**
     * 是否需要动画 默认开启动画
     */
    private var isNeedAnimation: Boolean = false

    private var mLineCount = 0

    private var mContent: CharSequence? = null

    /**
     * 展开文字的颜色
     */
    private var mExpandBtnColor = 0

    /**
     * 收起的文字的颜色
     */
    private var mFoldBtnColor = 0

    /**
     * 展开的文案
     */
    private var mExpandBtnString: String? = null

    /**
     * 收起的文案
     */
    private var mFoldBtnString: String? = null

    /**
     * 在收回和展开前面添加的内容
     */
    private val mEndExpandContent: String? = null

    /**
     * 在收回和展开前面添加的内容的字体颜色
     */
    private var mEndExpandTextColor = 0

    //是否AttachedToWindow
    private var isAttached = false

    private val mParserList: PriorityQueue<IParser> = PriorityQueue<IParser>(6
    ) { o1: IParser, o2: IParser -> o2.level() - o1.level() }

    init {
        init(context, attrs, defStyleAttr)
        movementMethod = LocalLinkMovementMethod.instance
        addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                if (!isAttached) doSetContent()
                isAttached = true
            }

            override fun onViewDetachedFromWindow(v: View) {
            }
        })
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs != null) {
            val tpa = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView, defStyleAttr, 0)

            mMaxLines = tpa.getInt(R.styleable.ExpandableTextView_etv_max_line, DEF_MAX_LINE)
            mNeedExpend = tpa.getBoolean(R.styleable.ExpandableTextView_etv_show_expand, true)
            mNeedFold = tpa.getBoolean(R.styleable.ExpandableTextView_etv_show_fold, true)
            isNeedAnimation = tpa.getBoolean(R.styleable.ExpandableTextView_etv_need_animation, true)

            mNeedAlwaysShowRight = tpa.getBoolean(R.styleable.ExpandableTextView_etv_always_showright, false)

            mFoldBtnString = tpa.getString(R.styleable.ExpandableTextView_etv_fold_text)
            mExpandBtnString = tpa.getString(R.styleable.ExpandableTextView_etv_expand_text)

            mExpandBtnColor = tpa.getColor(R.styleable.ExpandableTextView_etv_expand_color, Color.parseColor("#247FFF"))
            mEndExpandTextColor = tpa.getColor(R.styleable.ExpandableTextView_etv_end_color, Color.parseColor("#999999"))
            mFoldBtnColor = tpa.getColor(R.styleable.ExpandableTextView_etv_fold_color, Color.parseColor("#999999"))

            currentLines = mMaxLines
            tpa.recycle()
        }
        if (TextUtils.isEmpty(mExpandBtnString)) {
            mExpandBtnString = TEXT_EXPEND
        }
        if (TextUtils.isEmpty(mFoldBtnString)) {
            mFoldBtnString = TEXT_FOLD
        }

        paint.style = Paint.Style.FILL_AND_STROKE

//        mMoreArrow = ResourcesCompat.getDrawable(resources, R.mipmap.shop_ic_note_content_more_arr, null)
//        if (mMoreArrow != null) {
//            mMoreArrow!!.setBounds(0, 0, 24, 24)
//        }
    }

    /**
     * 设置内容
     */
    fun setContent(content: String?) {
        mContent = content
        if (isAttached) doSetContent()
    }

    fun addParser(parser: IParser) {
        mParserList.add(parser)
    }

    fun setExpand(expand: Boolean) {
        if (expand) {
            action(StatusType.STATUS_EXPAND)
        } else {
            action(StatusType.STATUS_FOLD)
        }
    }

    /**
     * 实际设置内容的
     */
    private fun doSetContent() {
        if (mContent == null) {
            return
        }
        currentLines = mMaxLines

        if (mWidth <= 0) {
            if (width > 0) mWidth = width - paddingLeft - paddingRight
        }

        if (mWidth <= 0) {
            if (retryTime > 10) {
                text = DEFAULT_CONTENT
            }
            this.post {
                retryTime++
                setContent(mContent.toString())
            }
        } else {
            // 将内容设置到控件中
            text = getRealContent(mContent.toString())
        }
    }

    private fun getRealContent(content: CharSequence?): SpannableStringBuilder {
        // 处理给定的数据，识别模式串
        mFormatData = formatData(content.toString())
        if (mFormatData.formattedContent.isNullOrEmpty())
            return SpannableStringBuilder("")

        // 用来计算内容的大小
        mDynamicLayout =
            DynamicLayout(
                mFormatData.formattedContent!!,
                paint, mWidth, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.0f,
                true
            )

        // 获取行数
        mLineCount = mDynamicLayout!!.lineCount

        onGetLineCountListener?.onGetLineCount(mLineCount, mLineCount > mMaxLines)

        return if (!mNeedExpend || mLineCount <= mMaxLines) {
            // 不需要展开功能 直接处理链接模块
            dealLink(mFormatData, false)
        } else {
            dealLink(mFormatData, true)
        }
    }

    /**
     * 对传入的数据进行正则匹配并处理
     */
    private fun formatData(content: String?): FormatData {
        val formatData = FormatData()
        if (mParserList.isEmpty() || content == null) {        // 如果无解析器，返回元数据
            formatData.formattedContent = content
            return formatData
        }
        val data = arrayListOf<FormatData.PositionData>()
        val convert: Map<String, String> = HashMap()
        var dealContent = content

        for (parser in mParserList) {
            val c = parser.parse(dealContent, data, convert)
            if (!TextUtils.isEmpty(c)) {
                dealContent = c
            }
        }

        if (convert.isNotEmpty() && dealContent != null) {        // 如果处理过程中有替换操作
            for ((key, value) in convert) {
                dealContent = dealContent!!.replace(key.toRegex(), value)
            }
        }
        formatData.formattedContent = dealContent
        formatData.positionData = data
        return formatData
    }

    private val expandEndContent: String
        /**
         * 设置最后的收起文案
         */
        get() = if (TextUtils.isEmpty(mEndExpandContent)) {
            String.format(
                Locale.getDefault(), "  %s",
                mFoldBtnString
            )
        } else {
            String.format(
                Locale.getDefault(), "  %s  %s",
                mEndExpandContent, mFoldBtnString
            )
        }

    private val hideEndContent: String
        /**
         * 设置展开的文案
         */
        get() {
            return if (TextUtils.isEmpty(mEndExpandContent)) {
                String.format(
                    Locale.getDefault(), if (mNeedAlwaysShowRight) "...  %s" else "...  %s",
                    mExpandBtnString
                )
            } else {
                String.format(
                    Locale.getDefault(), if (mNeedAlwaysShowRight) "...  %s  %s" else "...  %s  %s",
                    mEndExpandContent, mExpandBtnString
                )
            }
        }

    /**
     * 处理文字中的链接、@等
     */
    private fun dealLink(formatData: FormatData?, expandable: Boolean): SpannableStringBuilder {
        if (formatData == null || formatData.formattedContent.isNullOrEmpty() || mDynamicLayout == null) {
            return SpannableStringBuilder()
        }

        val ssb = SpannableStringBuilder()
        //获取存储的状态

            val isHide = mStatus === StatusType.STATUS_FOLD

            currentLines = if (isHide) {
                mMaxLines
            } else {
                mMaxLines + ((mLineCount - mMaxLines))
            }

        //处理折叠操作
        if (expandable) {
            if (currentLines in 1..< mLineCount) {
                val index = currentLines - 1
                val endPosition = mDynamicLayout!!.getLineEnd(index)
                val startPosition = mDynamicLayout!!.getLineStart(index)
                val lineWidth = mDynamicLayout!!.getLineWidth(index)

                Log.e(
                    TAG,
                    "index: $index, endPos: $endPosition, startPos:$startPosition, lineWidth:$lineWidth, view w:$width, real w: $mWidth"
                )
                val endString = hideEndContent

                //计算原内容被截取的位置下标
                val fitPosition =
                    getFitPosition(
                        formatData.formattedContent!!,
                        endString,
                        endPosition,
                        startPosition,
                        mWidth.toFloat(),
                        paint.measureText(endString),
                        0f
                    )
                Log.e(TAG, "fit position: $fitPosition")

                var substring: String = formatData.formattedContent!!.substring(0, fitPosition)
                if (substring.endsWith("\n")) {
                    substring = substring.substring(0, substring.length - "\n".length)
                }
                ssb.append(substring)

                if (mNeedAlwaysShowRight) {
                    //计算一下最后一行有没有充满
                    /*float lastLineWidth = 0;
                    for (int i = 0; i < index; i++) {
                        lastLineWidth += mDynamicLayout.getLineWidth(i);
                    }

                    lastLineWidth = lastLineWidth / (index);*/
                    val emptyWidth = mWidth - lineWidth - paint.measureText(endString)
                    if (emptyWidth > 0) {
                        val measureText = paint.measureText(SPACE)
                        var count = 0
                        while (measureText * count < emptyWidth) {
                            count++
                        }
                        Log.e(TAG, "count: $count")
                        count--
                        for (i in 0 until count) {
                            ssb.append(SPACE)
                        }
                    }
                }

                //在被截断的文字后面添加 "展开"
                ssb.append(endString)

                val expendLength = if (TextUtils.isEmpty(mEndExpandContent)) 0 else 2 + mEndExpandContent!!.length
                ssb.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            if (needRealExpandOrFold) {
                                action(StatusType.STATUS_EXPAND)
                            }
                            if (expandOrContractClickListener != null) {
                                expandOrContractClickListener!!.onClick(StatusType.STATUS_EXPAND)
                            }
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            ds.color = mExpandBtnColor
                            ds.isUnderlineText = false
                        }
                    },
                    ssb.length - mExpandBtnString!!.length - expendLength,
                    ssb.length,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )

                if (mMoreArrow != null) {
                    ssb.setSpan(
                        CenterImageSpan(mMoreArrow!!, ImageSpan.ALIGN_BASELINE),
                        ssb.length - 1,
                        ssb.length,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                }
            } else {
                ssb.append(formatData.formattedContent)
                if (mNeedFold) {
                    val endString = expandEndContent

                    if (mNeedAlwaysShowRight) {
                        //计算一下最后一行有没有充满
                        val index = mDynamicLayout!!.lineCount - 1
                        val lineWidth = mDynamicLayout!!.getLineWidth(index)
                        var lastLineWidth = 0f
                        for (i in 0 until index) {
                            lastLineWidth += mDynamicLayout!!.getLineWidth(i)
                        }
                        lastLineWidth /= (index)
                        val emptyWidth = lastLineWidth - lineWidth - paint.measureText(endString)
                        if (emptyWidth > 0) {
                            val measureText = paint.measureText(SPACE)
                            var count = 0
                            while (measureText * count < emptyWidth) {
                                count++
                            }
                            count--
                            for (i in 0 until count) {
                                ssb.append(SPACE)
                            }
                        }
                    }

                    ssb.append(endString)

                    val expendLength = if (TextUtils.isEmpty(mEndExpandContent)) 0 else 2 + mEndExpandContent!!.length
                    ssb.setSpan(
                        object : ClickableSpan() {
                            override fun onClick(widget: View) {
                                if (needRealExpandOrFold) {
                                    action(StatusType.STATUS_FOLD)
                                }
                                if (expandOrContractClickListener != null) {
                                    expandOrContractClickListener!!.onClick(StatusType.STATUS_FOLD)
                                }
                            }

                            override fun updateDrawState(ds: TextPaint) {
                                super.updateDrawState(ds)
                                ds.color = mFoldBtnColor
                                ds.isUnderlineText = false
                            }
                        },
                        ssb.length - mFoldBtnString!!.length - expendLength,
                        ssb.length,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                } else {
                    if (!TextUtils.isEmpty(mEndExpandContent)) {
                        ssb.append(mEndExpandContent)
                        ssb.setSpan(
                            ForegroundColorSpan(mEndExpandTextColor),
                            ssb.length - mEndExpandContent!!.length,
                            ssb.length,
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
        } else {
            ssb.append(formatData.formattedContent)
            if (!TextUtils.isEmpty(mEndExpandContent)) {
                ssb.append(mEndExpandContent)
                ssb.setSpan(
                    ForegroundColorSpan(mEndExpandTextColor),
                    ssb.length - mEndExpandContent!!.length,
                    ssb.length,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )
            }
        }

        val positionData = formatData.positionData
        if (positionData != null) {
            for (data in positionData) {
                if (ssb.length >= data.end) {
                    val fitPosition = ssb.length - hideEndContent.length
                    data.parser
                        .getSpan(ssb, data, fitPosition, mNeedExpend && expandable, currentLines < mLineCount)
                }
            }
        }
        //清除链接点击时背景效果
        highlightColor = Color.TRANSPARENT
        Log.e(TAG, ssb.toString())
        return ssb
    }

    /**
     * 执行展开和收回的动作
     */
    private fun action(status: StatusType) {
        if (status == mStatus) {
            return
        }
        mStatus = status
        val isHide = currentLines < mLineCount

        if (isNeedAnimation) {
            // 待实现动画
            currentLines = if (isHide) {
                mMaxLines + ((mLineCount - mMaxLines))
            } else {
                mMaxLines
            }
            text = getRealContent(mContent)
        } else {
            currentLines = if (isHide) {
                mMaxLines + ((mLineCount - mMaxLines))
            } else {
                mMaxLines
            }
            text = getRealContent(mContent)
        }
    }

    /**
     * 计算原内容被裁剪的长度
     *
     * @param content       原内容
     * @param endPosition   指定行最后文字的位置
     * @param startPosition 指定行文字开始的位置
     * @param lineWidth     指定行文字的宽度
     * @param endStringWith 最后添加的文字的宽度
     * @param offset        偏移量
     */
    private fun getFitPosition(
        content: String,
        endString: String, endPosition: Int, startPosition: Int, lineWidth: Float,
        endStringWith: Float, offset: Float
    ): Int {
        // 最后一行需要添加的文字的字数
        val position = ((lineWidth - (endStringWith + offset)) * (endPosition - startPosition)
                / lineWidth).toInt()

        if (position <= endString.length) return endPosition

        // 计算最后一行需要显示的正文的长度
        val measureText = paint.measureText(
            (content.substring(startPosition, startPosition + position))
        )

        Log.e(TAG, "measure text: $measureText")
        // 如果最后一行需要显示的正文的长度比最后一行的长减去“展开”文字的长度要短就可以了  否则加个空格继续算
        return if (measureText <= lineWidth - endStringWith) {
            startPosition + position
        } else {
            getFitPosition(content,
                endString, endPosition, startPosition, lineWidth, endStringWith, offset + paint.measureText(
                    SPACE
                )
            )
        }
    }

    /**
     * 绑定状态
     */
    fun setStatus(status: StatusType) {
        mStatus = status
    }

    class LocalLinkMovementMethod : LinkMovementMethod() {
        override fun onTouchEvent(
            widget: TextView,
            buffer: Spannable, event: MotionEvent
        ): Boolean {
            val action = event.action

            if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN
            ) {
                var x = event.x.toInt()
                var y = event.y.toInt()

                x -= widget.totalPaddingLeft
                y -= widget.totalPaddingTop

                x += widget.scrollX
                y += widget.scrollY

                val layout = widget.layout
                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x.toFloat())

                val link = buffer.getSpans(
                    off, off, ClickableSpan::class.java
                )

                if (link.isNotEmpty()) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget)
                    } else {
                        Selection.setSelection(
                            buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0])
                        )
                    }

                    if (widget is ExpandableTextView) {
                        widget.linkHit = true
                    }
                    return true
                } else {
                    Selection.removeSelection(buffer)
                    Touch.onTouchEvent(widget, buffer, event)
                    return false
                }
            }
            return Touch.onTouchEvent(widget, buffer, event)
        }

        companion object {
            private var sInstance: LocalLinkMovementMethod? = null
            val instance: LocalLinkMovementMethod?
                get() {
                    if (sInstance == null) sInstance = LocalLinkMovementMethod()

                    return sInstance
                }
        }
    }

    var dontConsumeNonUrlClicks: Boolean = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        linkHit = false
        val res = super.onTouchEvent(event)

        if (dontConsumeNonUrlClicks) return linkHit

        //防止选择复制的状态不消失
        if (action == MotionEvent.ACTION_UP) {
            this.setTextIsSelectable(false)
        }

        return res
    }

    interface OnGetLineCountListener {
        /**
         * lineCount 预估可能占有的行数
         * canExpand 是否达到可以展开的条件
         */
        fun onGetLineCount(lineCount: Int, canExpand: Boolean)
    }

    var onGetLineCountListener: OnGetLineCountListener? = null

    interface OnExpandOrFoldClickListener {
        fun onClick(type: StatusType?)
    }

    fun canFold(): Boolean {
        return mNeedFold
    }

    fun setCanFold(canFold: Boolean) {
        this.mNeedFold = canFold
    }

    fun canExpand(): Boolean {
        return mNeedExpend
    }

    fun setCanExpend(canExpend: Boolean) {
        this.mNeedExpend = canExpend
    }

    override fun setMaxLines(maxLines: Int) {
        if (this.mMaxLines != maxLines) {
            this.mMaxLines = maxLines
            doSetContent()
        }
    }

    override fun getMaxLines(): Int {
        return mMaxLines
    }

    fun setExpandBtnColor(expandBtnColor: Int) {
        this.mExpandBtnColor = expandBtnColor
    }

    fun setFoldBtnColor(foldBtnColor: Int) {
        this.mFoldBtnColor = foldBtnColor
    }

    fun setExpandBtnString(expandBtnString: String?) {
        this.mExpandBtnString = expandBtnString
    }

    fun setFoldBtnString(foldBtnString: String?) {
        this.mFoldBtnString = foldBtnString
    }

    fun setAlwaysShowRight(alwaysShowRight: Boolean) {
        this.mNeedAlwaysShowRight = alwaysShowRight
    }

    fun setNeedRealExpandOrFold(needRealExpandOrFold: Boolean) {
        this.needRealExpandOrFold = needRealExpandOrFold
    }

    fun setExpandOrContractClickListener(expandOrContractClickListener: OnExpandOrFoldClickListener?) {
        this.expandOrContractClickListener = expandOrContractClickListener
    }
}