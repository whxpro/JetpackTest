package com.whx.jetpacktest.nav.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whx.jetpacktest.BaseFragment
import com.whx.jetpacktest.databinding.FragmentNavCommonBinding
import com.whx.jetpacktest.nav.NavManager

class NFragmentD : BaseFragment() {

    companion object {
        const val DATA_KEY = "data_key"
    }

    private lateinit var binding: FragmentNavCommonBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNavCommonBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.centerText.text = arguments?.getString(DATA_KEY) ?: "error"
        binding.centerText.setOnClickListener {
            NavManager.backToMain()
        }
    }
}