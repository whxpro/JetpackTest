package com.whx.jetpacktest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    private var TAG = "BaseFragment"

    init {
        TAG = this.javaClass.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "---onCreate---")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "---onCreateView---")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "---onActivityCreated---")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        Log.i(TAG, "---onStart---")
        super.onStart()
    }
    override fun onResume() {
        Log.i(TAG, "---onResume---")
        super.onResume()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        Log.i(TAG, "---setUserVisibleHint isVisibleToUser = $isVisibleToUser---")
        super.setUserVisibleHint(isVisibleToUser)
    }

    override fun onPause() {
        Log.i(TAG, "---onPause---")
        super.onPause()
    }

    override fun onStop() {
        Log.i(TAG, "---onStop---")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.i(TAG, "---onDestroyView---")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.i(TAG, "---onDestroy---")
        super.onDestroy()
    }
}