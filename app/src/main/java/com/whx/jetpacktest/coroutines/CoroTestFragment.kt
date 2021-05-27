package com.whx.jetpacktest.coroutines

import androidx.fragment.app.viewModels
import com.whx.jetpacktest.BaseFragment

class CoroTestFragment : BaseFragment() {
    private val vm by viewModels<CoroViewModel>()
}