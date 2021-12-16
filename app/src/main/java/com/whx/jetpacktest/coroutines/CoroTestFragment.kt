package com.whx.jetpacktest.coroutines

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import com.whx.jetpacktest.BaseFragment

class CoroTestFragment : BaseFragment() {
    private val vm by viewModels<CoroViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}