package com.whx.jetpacktest.widget.expand

import com.whx.jetpacktest.widget.expand.parser.IParser

/**
 * 记录可以点击的内容 和 位置
 */
class FormatData {
    var formattedContent: String? = null
    var positionData: List<PositionData>? = null

    class PositionData(var start: Int, var end: Int, var url: String, parser: IParser) {
        private var mParser: IParser

        init {
            this.mParser = parser
        }

        var parser: IParser
            get() = mParser
            set(mParser) {
                this.mParser = mParser
            }
    }
}