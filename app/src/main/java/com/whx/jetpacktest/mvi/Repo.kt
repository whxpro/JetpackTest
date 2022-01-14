package com.whx.jetpacktest.mvi

import android.util.Log
import com.whx.jetpacktest.api.ApiInterface
import com.whx.jetpacktest.api.PicsumBean
import com.whx.jetpacktest.net.NetworkManager
import com.whx.jetpacktest.viewmodel.Meizi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.await

class Repo(private val dispatcher: CoroutineDispatcher) {

    suspend fun fetchImageData(pageNum: Int): List<PicsumBean>? {
        var list: List<PicsumBean>? = null
        withContext(dispatcher) {
            try {
                list = NetworkManager.getApiService("https://picsum.photos/", ApiInterface.Image::class.java)
                    .getUrlsFromPicsum(pageNum, 20).await()
            } catch (e: Exception) {
                Log.e("--------", "error: \n", e)
            }
        }
        return list
    }

    suspend fun fetchImageDataFlow(pageNum: Int): List<PicsumBean> {
        var list: List<PicsumBean>
        withContext(dispatcher) {
            list = NetworkManager.getApiService("https://picsum.photos/", ApiInterface.Image::class.java)
                    .getUrlsFromPicsum(pageNum, 20).await()
        }
        return list
    }
}