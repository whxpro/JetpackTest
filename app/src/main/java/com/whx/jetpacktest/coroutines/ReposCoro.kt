package com.whx.jetpacktest.coroutines

import com.whx.jetpacktest.api.ApiInterface
import com.whx.jetpacktest.net.NetworkManager
import com.whx.jetpacktest.viewmodel.Data
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.await

class ReposCoro(private val dispatcher: CoroutineDispatcher) {

    suspend fun getImageData(page: String): Data {
        return NetworkManager.getApiService("https://gank.io/", ApiInterface.Image::class.java)
            .getUrlsA(page).await()
    }

    suspend fun fetchImageData(page: String): List<Map<String, Any>> {
        var list: List<Map<String, Any>>
        withContext(dispatcher) {
            val netData = NetworkManager.getApiService("https://gank.io/", ApiInterface.Image::class.java)
                .getUrlsA(page).await()
            list = netData.results
        }
        return list
    }
}