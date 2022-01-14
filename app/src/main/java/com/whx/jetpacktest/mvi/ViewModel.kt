package com.whx.jetpacktest.mvi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whx.jetpacktest.api.PicsumBean
import com.whx.jetpacktest.viewmodel.Meizi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val mRepo = Repo(Dispatchers.Default)

    private val _viewStates = MutableLiveData<MainViewState>()
    val viewStates: LiveData<MainViewState> = _viewStates

    private val _viewEvents = SingleLiveEvent<MainViewEvent?>()
    val viewEvents: LiveData<MainViewEvent?> = _viewEvents

    init {
        emit(MainViewState(FetchStatus.NotFetch, emptyList()))
    }

    private fun emit(state: MainViewState) {
        _viewStates.value = state
    }

    private fun emit(event: MainViewEvent) {
        _viewEvents.setValue(event)
    }

    fun fetchImage(pageNum: Int) {
        _viewStates.setState {
            this.copy(fetchStatus = FetchStatus.Loading)
        }
        viewModelScope.launch {
            when (val imageList = mRepo.fetchImageData(pageNum)) {
                null -> {
                    _viewStates.setState { this.copy(fetchStatus = FetchStatus.Error, newsList = emptyList()) }
                }
                else -> {
                    withContext(Dispatchers.Default) {
                        _viewStates.postState {
                            this.copy(fetchStatus = FetchStatus.Fetched, newsList = imageList.map {
                                ImageUiState(it) { _, isLike ->
                                    dealLike(it, isLike)
                                }
                            })
                        }
                    }
                }
            }
        }
    }

    private fun dealLike(mz: PicsumBean, isLike: Boolean) {
        mz.liked = isLike
    }


    private val _uiState = MutableStateFlow(MainViewState(FetchStatus.NotFetch, emptyList()))
    val uiState: StateFlow<MainViewState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    fun fetchImageFlow(pageNum: Int) {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            Log.e("--------", "error: \n", e)
            _uiState.update { it.copy(fetchStatus = FetchStatus.Error, newsList = emptyList()) }
        }

        _uiState.update { it.copy(fetchStatus = FetchStatus.Loading) }

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(exceptionHandler) {
            val imgList = mRepo.fetchImageDataFlow(pageNum)
            withContext(Dispatchers.Default) {
                _uiState.update {
                    it.copy(fetchStatus = FetchStatus.Fetched, newsList = imgList.map { bean ->
                        ImageUiState(bean) { _, isLike ->
                            dealLike(bean, isLike)
                        }
                    })
                }
            }
        }
    }
}