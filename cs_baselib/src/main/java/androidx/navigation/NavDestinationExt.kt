package androidx.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.NavContainerFragment
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import com.guadou.lib_baselib.utils.log.YYLogUtils

internal fun NavDestination.removeFromParent() {
    this.parent = null
}


fun NavHostFragment.showLog() {

    val res = StringBuilder("NavGraph")

    navController.mBackStack.forEachIndexed { index, entry ->
        entry.destination.let { des ->
            if (des is FragmentNavigator.Destination) {
                val frag = childFragmentManager.fragments[index - 1]
                res.append("\n +- ${des.className}\n   [tag]${frag.tag}  [hash]${(frag as NavContainerFragment).mRealFragment.hashCode()}")
            }
        }
    }

    YYLogUtils.w("res:${res.toString()}")
}

//获取当前的Fragment
fun NavHostFragment.getCurFragment(): Fragment? {

    val entry: NavBackStackEntry = navController.mBackStack.last
    val des = entry.destination

    return if (des is FragmentNavigator.Destination) {
        val frag = childFragmentManager.fragments[navController.mBackStack.size - 2]
        (frag as NavContainerFragment).mRealFragment
    } else {
        null
    }
}

//获取全部的Fragment
fun NavHostFragment.getAllFragments(): List<Fragment> {
    val list = arrayListOf<Fragment>()
    navController.mBackStack.forEachIndexed { index, entry ->
        entry.destination.let { des ->
            if (des is FragmentNavigator.Destination) {
                val frag = childFragmentManager.fragments[index - 1]
                list.add((frag as NavContainerFragment).mRealFragment)
            }
        }
    }
    return list
}