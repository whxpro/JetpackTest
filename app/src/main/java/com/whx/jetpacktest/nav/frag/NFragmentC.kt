package com.whx.jetpacktest.nav.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.whx.jetpacktest.BaseFragment
import com.whx.jetpacktest.R
import com.whx.jetpacktest.nav.NavManager
import kotlinx.android.synthetic.main.fragment_nav_common.*

class NFragmentC : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nav_common, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        center_text.text = "fragment c"
        center_text.setOnClickListener {
            NavManager.navigate(NFragmentD::class, bundleOf(NFragmentD.DATA_KEY to "what the fuck"))
        }
    }
}