package com.whx.jetpacktest.nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.*
import com.whx.jetpacktest.R
import kotlin.reflect.KClass

object NavManager {
    private val dfltOptions = navOptions {
        anim {
            enter = R.anim.anim_right_in            // 下一页进入动画
            exit = R.anim.anim_left_out             // 上一页退出动画

            popEnter = R.anim.anim_left_in          // 下一页返回时上一页的进入动画
            popExit = R.anim.anim_right_out         // 下一页退出动画
        }
    }

    private lateinit var mNavController: NavController

    @JvmStatic
    fun init(act: FragmentActivity, hostId: Int) {
        mNavController = act.findNavController(hostId)
    }

    fun backToMain(clearSelf: Boolean = true) {
        goBackTo(mNavController.graph.startDestinationId, clearSelf)
    }

    fun goBackTo(destinationId: Int, clearSelf: Boolean = false) {
        mNavController.popBackStack(destinationId, clearSelf)
    }

    fun navigate(target: KClass<out Fragment>,
                 data: Bundle? = null,
                 navOptions: NavOptions? = dfltOptions,
                 extras: Navigator.Extras? = null,
                 destinationId: Int = target.hashCode()) {
        try {
            val destination =
                NavHelper.createFragmentDestination(mNavController, destinationId, target)

            if (destination.id != mNavController.graph.findNode(destinationId)?.id) {
                mNavController.graph.addDestination(destination)
            }
            mNavController.navigate(destinationId, data, navOptions, extras)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}