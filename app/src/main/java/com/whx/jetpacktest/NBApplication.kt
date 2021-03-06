package com.whx.jetpacktest

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.didichuxing.doraemonkit.DoraemonKit
import com.facebook.drawee.backends.pipeline.Fresco

class NBApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        mContext = this

        DoraemonKit.install(this)
        Fresco.initialize(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mContext: Context? = null

        fun getAppContext() = mContext!!
    }
}