package com.whx.jetpacktest.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private val mClient = OkHttpClient()
        .newBuilder()
        .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
        .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
        .build()

    private val mBuilder = Retrofit.Builder()
        .client(mClient)
        .addConverterFactory(GsonConverterFactory.create())

    @JvmStatic
    fun <A> getApiService(baseUrl: String, apiClass: Class<A>): A {
        return mBuilder.baseUrl(baseUrl).build().create(apiClass)
    }
}