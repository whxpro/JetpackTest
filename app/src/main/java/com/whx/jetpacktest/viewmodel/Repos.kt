package com.whx.jetpacktest.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.whx.jetpacktest.api.ApiInterface
import com.whx.jetpacktest.net.NetworkManager
import com.whx.jetpacktest.net.SSLSocketClient
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repos {
    companion object {
        fun getUrlData(page: String): MutableLiveData<Data> {
            val tmp = MutableLiveData<Data>()

            NetworkManager.getApiService("https://gank.io/", ApiInterface.Image::class.java)
                .getUrls(page)
                .enqueue(object : Callback<Data> {
                    override fun onResponse(call: Call<Data>, response: Response<Data>) {
                        tmp.value = response.body()
                    }

                    override fun onFailure(call: Call<Data>, t: Throwable) {
                        Log.w("Repos---------", t.message.toString())
                    }
                })

            return tmp
        }
    }
}