package com.whx.jetpacktest.widget.compress

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

class Compressor private constructor(builder: Builder) {
    companion object {
        private const val DEFAULT_DISK_CACHE_DIR = "compress_cache"

        private const val TAG = "Compress"

        fun with(context: Context): Builder {
            return Builder(context)
        }
    }

    private var mTargetDir = ""
    private var mLeastSize = 100
    private var mStreamProviders: MutableList<InputStreamProvider>? = null

    init {
        mTargetDir = builder.targetDir
        mLeastSize = builder.leastSize
        mStreamProviders = builder.mStreamProviders
    }

    @Throws(IOException::class)
    private fun get(context: Context): List<File> {
        val results: MutableList<File> = ArrayList()
        mStreamProviders?.forEach { isp ->
            compress(context, isp)?.let { results.add(it) }
        }
        mStreamProviders?.clear()
        return results
    }

    @Throws(IOException::class)
    private fun compress(context: Context, streamProvider: InputStreamProvider): File? {
        try {
            return compressReal(context, streamProvider)
        } finally {
            streamProvider.close()
        }
    }

    private fun compressReal(context: Context, streamProvider: InputStreamProvider): File? {
        val srcPath = streamProvider.path ?: return null

        val imgSuffix = Checker.extSuffix(streamProvider)
        val outFile = getImageCacheFile(context, imgSuffix)

        return if (Checker.needCompress(mLeastSize, srcPath) && outFile != null) {
            Engine(streamProvider, outFile, imgSuffix).compress()
        } else {
            File(srcPath)
        }
    }

    private fun getImageCacheFile(context: Context, suffix: String): File? {
        if (mTargetDir.isBlank()) {
            mTargetDir = getImageCacheDir(context)?.absolutePath ?: return null
        }
        val cacheBuilder: String =
            mTargetDir + "/" + System.currentTimeMillis() + (Math.random() * 1000).toInt() + if (suffix.isBlank()) ".jpg" else suffix

        val file = File(cacheBuilder)
        file.createNewFile()
        return file
    }

    private fun getImageCacheDir(context: Context): File? {
        return getImageCacheDir(context, DEFAULT_DISK_CACHE_DIR)
    }

    @Suppress("SameParameterValue")
    private fun getImageCacheDir(context: Context, cacheName: String): File? {
        val cacheDir = context.externalCacheDir
        if (cacheDir != null) {
            val result = File(cacheDir, cacheName)
            return if (!result.mkdirs() && (!result.exists() || !result.isDirectory)) {
                // File wasn't able to create a directory, or the result exists but not a directory
                null
            } else result
        }
        if (Log.isLoggable(TAG, Log.ERROR)) {
            Log.e(TAG, "default disk cache dir is null")
        }
        return null
    }

    class Builder(private val mContext: Context) {
        internal val mStreamProviders = mutableListOf<InputStreamProvider>()
        internal var leastSize = 100
        internal var targetDir = ""

        fun build(): Compressor {
            return Compressor(this)
        }

        fun get(): List<File> {
            return build().get(mContext)
        }

        fun setTargetPath(targetPath: String): Builder {
            this.targetDir = targetPath
            return this
        }

        fun setLeastSize(leastSize: Int): Builder {
            this.leastSize = leastSize
            return this
        }

        fun <T> load(list: List<T>): Builder {
            for (src in list) {
                when (src) {
                    is String -> {
                        load(src as String)
                    }
                    is File -> {
                        load(src as File)
                    }
                    is Uri -> {
                        load(src as Uri)
                    }
                    else -> {
                        throw IllegalArgumentException("Incoming data type exception, it must be String, File, Uri or Bitmap")
                    }
                }
            }
            return this
        }

        fun load(uri: Uri): Builder {
            mStreamProviders.add(object : InputStreamAdapter() {
                @Throws(IOException::class)
                override fun openInternal(): InputStream? {
                    return mContext.contentResolver.openInputStream(uri)
                }

                override val path: String?
                    get() = uri.path
            })
            return this
        }

        fun load(string: String?): Builder {
            mStreamProviders.add(object : InputStreamAdapter() {
                @Throws(IOException::class)
                override fun openInternal(): InputStream {
                    return FileInputStream(string)
                }

                override val path: String?
                    get() = string
            })
            return this
        }

        fun load(file: File): Builder {
            mStreamProviders.add(object : InputStreamAdapter() {
                @Throws(IOException::class)
                override fun openInternal(): InputStream {
                    return FileInputStream(file)
                }

                override val path: String?
                    get() = file.absolutePath
            })
            return this
        }
    }
}