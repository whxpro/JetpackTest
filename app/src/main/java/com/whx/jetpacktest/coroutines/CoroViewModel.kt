package com.whx.jetpacktest.coroutines

import androidx.lifecycle.*
import com.whx.jetpacktest.viewmodel.Meizi
import kotlinx.coroutines.launch
import java.lang.Exception

class CoroViewModel : ViewModel() {
    private val repo = ReposCoro()

    private val pageLD by lazy { MutableLiveData<String>() }
    val imagesLD = pageLD.switchMap { page ->
        liveData {
            try {
                val data = repo.getImageData(page)
                val mzs = data.results.map {
                    Meizi(it["who"].toString(), it["url"].toString(), false)
                }
                emit(mzs)
            } catch (e: Exception) {
                emit(null)
            }
        }
    }

    val adapter by lazy {
        RecyAdapter()
    }

    fun getImageData(page: String) {
        pageLD.value = page
    }
}