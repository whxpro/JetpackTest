package com.whx.jetpacktest.nav.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.whx.jetpacktest.BaseFragment
import com.whx.jetpacktest.databinding.FragmentNavCommonBinding
import com.whx.jetpacktest.nav.NavManager

class NFragmentC : BaseFragment() {
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
        binding.centerText.text = "fragment c"
        binding.centerText.setOnClickListener {
            NavManager.navigate(NFragmentD::class, bundleOf(NFragmentD.DATA_KEY to "what the fuck"))
        }
    }
}