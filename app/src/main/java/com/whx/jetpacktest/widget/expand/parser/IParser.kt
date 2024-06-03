package com.whx.jetpacktest.widget.expand.parser

import android.text.SpannableStringBuilder
import com.whx.jetpacktest.widget.expand.FormatData.PositionData


interface IParser {
    fun parse(content: String?, positionData: List<PositionData?>?, convert: Map<String, String>?): String?

    fun getSpan(
        content: SpannableStringBuilder?,
        positionData: PositionData?,
        fitPos: Int,
        shouldExpand: Boolean,
        currentLineLess: Boolean
    ): Any?

    fun level(): Int
}