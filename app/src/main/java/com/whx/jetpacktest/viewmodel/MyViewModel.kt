package com.whx.jetpacktest.viewmodel

import androidx.lifecycle.*

class MyViewModel(private val autoRefresh: AutoRefresh) : ViewModel() {
    val id = MutableLiveData<String>()
    var response: LiveData<Data>? = null

    private val beans = arrayListOf<Map<String, Any>>()

    init {
        response = id.switchMap {input ->
            if (input == null) {
                MutableLiveData()
            } else {
                Repos.getUrlData(input)
            }
        }
    }

    val adapter by lazy {
        VMRecyclerAdapter(beans)
    }

    fun notifyChange(data: List<Map<String, Any>>) {
        beans.clear()
        beans.addAll(data)
        autoRefresh.value?.set(true)
    }
}

class MyViewModelFactory(private val autoRefresh: AutoRefresh) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (ViewModel::class.java.isAssignableFrom(modelClass)) {
            return MyViewModel(autoRefresh) as T
        }
        return super.create(modelClass)
    }
}