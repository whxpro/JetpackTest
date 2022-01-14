package com.whx.jetpacktest.api

import com.whx.jetpacktest.viewmodel.Data
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    interface Image {
        @GET("api/data/%E7%A6%8F%E5%88%A9/10/{page}")
        fun getUrls(@Path("page") page: String): Call<Data>

        @GET("api/data/%E7%A6%8F%E5%88%A9/10/{page}")
        fun getUrlsA(@Path("page") page: String): Call<Data>

        @GET("api/data/%E7%A6%8F%E5%88%A9/10/{page}")
        fun getUrlsB(@Path("page") page: String): Call<Data?>

        @GET("v2/list")
        fun getUrlsFromPicsum(@Query("page") pageNum: Int, @Query("limit") pageSize: Int): Call<List<PicsumBean>>
    }
}