package com.guadou.lib_baselib.view.nav

import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment

/**
 * 不生效 (使用在BaseFragment中保存View的实例的办法实现)
 */
class KeepStateNavHostFragment : NavHostFragment() {

    override fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination> {
        return KeepStateNavigator(requireContext(), childFragmentManager, id)
    }

}