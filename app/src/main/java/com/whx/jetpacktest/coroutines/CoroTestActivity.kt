package com.whx.jetpacktest.coroutines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.databinding.ActivityRecyclerBinding
import kotlin.math.max

class CoroTestActivity : BaseActivity() {
    private val imageVM by viewModels<CoroViewModel>()
    private var page = 1

    private lateinit var binding: ActivityRecyclerBinding

    init {
        lifecycleScope.launchWhenCreated {
            loadData("1")

            imageVM.imagesLD.observe(this@CoroTestActivity, Observer {
                it?.let {
                    imageVM.adapter.addData(it)
                    page++
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecyclerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        binding.recyclerView.adapter = imageVM.adapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val lm = recyclerView.layoutManager as StaggeredGridLayoutManager

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val vlast = lm.findLastVisibleItemPositions(null)

                    if (max(
                            vlast[0],
                            vlast[1]
                        ) in (imageVM.adapter.data.size - 2)..(imageVM.adapter.data.size)
                    ) {
                        loadData(page.toString())
                    }
                }
            }
        })
    }

    private fun loadData(page: String) {
        Log.e("-----", page)
        imageVM.getImageData(page)
    }
}