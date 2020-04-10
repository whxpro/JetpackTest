package com.whx.jetpacktest

import com.whx.jetpacktest.viewmodel.Data
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    interface Image {
        @GET("api/data/%E7%A6%8F%E5%88%A9/10/{page}")
        fun getUrls(@Path("page") page: String): Call<Data>
    }
}