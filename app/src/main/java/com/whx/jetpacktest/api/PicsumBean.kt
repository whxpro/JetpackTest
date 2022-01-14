package com.whx.jetpacktest.api

import com.google.gson.annotations.SerializedName

class PicsumBean(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    @SerializedName("download_url")
    val downloadUrl: String,
    var liked: Boolean = false
)