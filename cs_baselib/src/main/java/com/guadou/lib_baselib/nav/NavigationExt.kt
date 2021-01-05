package com.guadou.lib_baselib.nav

import androidx.fragment.app.Fragment
import androidx.navigation.NavController

/**
 * Navigation 拓展函数
 *
 * 获取当前 Fragment 的 NavController
 * Create by Robbin at 2020/7/13
 */
fun Fragment.nav(): NavController {
    return NavHostFragment.findNavController(this)
}
