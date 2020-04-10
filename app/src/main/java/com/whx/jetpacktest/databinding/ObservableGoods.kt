package com.whx.jetpacktest.databinding

import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat

class ObservableGoods(name: String, detail: String, price: Float) {

    var name: ObservableField<String> = ObservableField(name)
    var detail: ObservableField<String> = ObservableField(detail)
    var price: ObservableFloat = ObservableFloat(price)

}