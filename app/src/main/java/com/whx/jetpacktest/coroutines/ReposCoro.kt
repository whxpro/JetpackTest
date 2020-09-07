package com.whx.jetpacktest.coroutines

import com.whx.jetpacktest.api.ApiInterface
import com.whx.jetpacktest.net.NetworkManager
import com.whx.jetpacktest.viewmodel.Data
import retrofit2.await

class ReposCoro {

    suspend fun getImageData(page: String): Data {
        return NetworkManager.getApiService("https://gank.io/", ApiInterface.Image::class.java)
            .getUrlsA(page).await()
    }
}