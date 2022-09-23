package com.guadou.lib_baselib.utils.statusBarHost

import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity

//val FragmentActivity.windowInsetsCompat: WindowInsetsCompat?
//    get() = ViewCompat.getRootWindowInsets(findViewById<FrameLayout>(android.R.id.content))
//
////获取状态栏高度
//fun FragmentActivity.getStatusBarsHeight(): Int {
//    val windowInsetsCompat = windowInsetsCompat ?: return 0
//    return windowInsetsCompat.getInsets(WindowInsetsCompat.Type.statusBars()).top
//}
//
////获取导航栏高度
//fun FragmentActivity.getNavigationBarsHeight(): Int {
//    val windowInsetsCompat = windowInsetsCompat ?: return 0
//    return windowInsetsCompat.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
//}
