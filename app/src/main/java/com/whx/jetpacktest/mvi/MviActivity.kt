package com.whx.jetpacktest.mvi

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R
import com.whx.jetpacktest.utils.dp
import com.whx.jetpacktest.utils.toast
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MviActivity : BaseActivity() {

    private val mViewModel by viewModels<MainViewModel>()
    private val mAdapter by lazy { RvAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_mvi_test)
        val rv = findViewById<RecyclerView>(R.id.recycler_view)
        rv.apply {
            layoutManager = LinearLayoutManager(this@MviActivity)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.bottom = 10.dp().toInt()
                }
            })
            adapter = mAdapter
        }
        initObserve()
        initFlowCollect()

        loadData()
    }

    private fun loadData() {
//        mViewModel.fetchMeizi(1)
        mViewModel.fetchImageFlow(1)
    }

    private fun initObserve() {
        mViewModel.viewStates.run {
            observeState(this@MviActivity, MainViewState::newsList) {
                mAdapter.submitList(it)
            }
            observeState(this@MviActivity, MainViewState::fetchStatus) {
                when (it) {
                    FetchStatus.Loading -> {
                        toast("loading")
                    }
                    FetchStatus.Error -> {
                        toast("error")
                    }
                }
            }
        }

        mViewModel.viewEvents.observe(this) {

        }
    }

    private fun initFlowCollect() {
        mViewModel.uiState.launchAndCollectIn(owner = this) { (state, list) ->
            Log.e("--------", "$state")
            when (state) {
                FetchStatus.Loading -> {
                    toast("loading")
                }
                FetchStatus.Error -> {
                    toast("error")
                }
            }
            mAdapter.submitList(list)
        }

        /*
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                mViewModel.uiState.collect { (state, list) ->
                    Log.e("--------", "$state")
                    when (state) {
                        FetchStatus.Loading -> {
                            toast("loading")
                        }
                        FetchStatus.Error -> {
                            toast("error")
                        }
                    }
                    mAdapter.submitList(list)
                }
                // 注意：在同一个协程中，上面的collect 在协程被取消前会一直挂起，所以后面的代码不会执行！！！


                /*mViewModel.uiState.collect { (state, list) ->
                    Log.e("---------", "another collect: $state")
                }*/
                Log.e("--------", "collect end")
            }
        }
        lifecycleScope.launch {
            mViewModel.uiState.flowWithLifecycle(lifecycle).collect { (state, list) ->
                Log.e("---------", "another collect: $state")
            }
        }
        */
    }
}