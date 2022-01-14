package com.whx.jetpacktest.mvi

import com.whx.jetpacktest.api.PicsumBean


enum class FetchStatus {
    NotFetch,
    Loading,
    Error,
    Fetched
}

data class MainViewState(val fetchStatus: FetchStatus, val newsList: List<ImageUiState>)

data class ImageUiState(val picsumBean: PicsumBean, val onClickLike: (String, Boolean)-> Unit)

sealed class MainViewEvent {
    data class ShowSnackBar(val msg: String) : MainViewEvent()
    data class ShowToast(val msg: String) : MainViewEvent()
}

