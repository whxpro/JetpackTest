package com.whx.jetpacktest.databinding

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class Goods(@JvmField @Bindable var name: String, private var detail: String?, private var price: Float?) : BaseObservable() {

    fun setName(name: String) {
        this.name = name
        notifyPropertyChanged(BR.name)
    }

    @Bindable
    fun getDetail(): String? {
        return detail
    }

    fun setDetail(detail: String?) {
        this.detail = detail
        notifyChange()
    }

    fun getPrice(): Float? = price

    fun setPrice(price: Float?) {
        this.price = price
    }
}