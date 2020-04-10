package com.whx.jetpacktest.viewmodel

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.whx.jetpacktest.BaseActivity
import com.whx.jetpacktest.R
import com.whx.jetpacktest.databinding.ActivityViewmodelBinding
import kotlinx.android.synthetic.main.activity_viewmodel.*

class ViewModelActivity : BaseActivity() {

    private lateinit var viewDatabinding: ActivityViewmodelBinding
    private var viewmodel: MyViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDatabinding = DataBindingUtil.setContentView(this, R.layout.activity_viewmodel)
        viewDatabinding.activity = this
        viewDatabinding.autoRefresh = AutoRefresh(ObservableBoolean(false))

        viewmodel = ViewModelProviders.of(this, MyViewModelFactory(viewDatabinding.autoRefresh!!)).get(MyViewModel::class.java)
        viewmodel?.response?.observe(this, Observer {
            viewmodel?.notifyChange(it.results)
        })
        viewDatabinding.adapter = viewmodel?.adapter
    }

    fun confirmClick() {
        viewmodel?.id?.value = edit_text.text.toString()
    }
}