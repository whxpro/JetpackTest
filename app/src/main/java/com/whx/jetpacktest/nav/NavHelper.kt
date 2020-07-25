package com.whx.jetpacktest.nav

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import kotlin.reflect.KClass

object NavHelper {

    @JvmStatic
    fun createFragmentDestination(navController: NavController,
                                  destinationId: Int,
                                  target: KClass<out Fragment>): FragmentNavigator.Destination {
        return FragmentNavigatorDestinationBuilder(
            navController.navigatorProvider.getNavigator(FragmentNavigator::class.java),
            destinationId,
            target
        ).build()
    }
}