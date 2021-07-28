package com.whx.jetpacktest.widget.compress

import java.io.IOException
import java.io.InputStream

abstract class InputStreamAdapter : InputStreamProvider {

    private var inputStream: InputStream? = null

    @Throws(IOException::class)
    override fun open(): InputStream? {
        close()
        inputStream = openInternal()
        return inputStream
    }

    @Throws(IOException::class)
    abstract fun openInternal(): InputStream?

    override fun close() {
        if (inputStream != null) {
            try {
                inputStream!!.close()
            } catch (ignore: IOException) {
            } finally {
                inputStream = null
            }
        }
    }
}