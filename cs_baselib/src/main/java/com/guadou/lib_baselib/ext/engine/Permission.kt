package com.guadou.lib_baselib.ext.engine

import android.app.Activity
import androidx.fragment.app.Fragment
import com.guadou.lib_baselib.ext.toast
import com.yanzhenjie.permission.AndPermission

/**
 * 申请权限的引擎类
 * Activity的申请权限
 */
fun Activity.sendPermissions(
    vararg permissions: String,
    deniedStr: String = "You have denied permission. You cannot open this module or use this function",
    block: () -> Unit
) {
    AndPermission.with(this).runtime()
        .permission(permissions)
        .onGranted {
            block()
        }
        .onDenied {
            toast(deniedStr)
        }
        .start()
}

/**
 * 申请权限的引擎
 * Fragment的申请权限
 */
fun Fragment.sendtPermissions(
    vararg permissions: String,
    deniedStr: String = "You have denied permission. You cannot open this module or use this function",
    block: () -> Unit
) {
    AndPermission.with(this).runtime()
        .permission(permissions)
        .onGranted {
            block()
        }
        .onDenied {
            toast(deniedStr)
        }
        .start()
}