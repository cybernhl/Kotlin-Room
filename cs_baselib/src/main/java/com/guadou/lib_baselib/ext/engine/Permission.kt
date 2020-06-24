package com.guadou.lib_baselib.ext.engine

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.view.FangIOSDialog
import com.yanzhenjie.permission.AndPermission

/**
 * 申请权限的引擎类
 * Activity的申请权限
 */
fun Activity.permissionRequest(
    vararg permissions: String,
    deniedStr: String = "You have denied permission. You cannot open this module or use this function",
    rationaleStr: String = "You must agree to this permission to use this module or function. Are you sure you want to agree to this permission?",
    block: () -> Unit
) {
    AndPermission.with(this).runtime()
        .permission(permissions)
        .rationale { _, _, executor ->

            //使用弹窗再次确认
            FangIOSDialog(this).apply {
                setTitle("Alert")
                setMessage(rationaleStr)
                setNegativeButton("No") {
                    executor.cancel()
                    dismiss()
                }
                setPositiveButton("Yes") {
                    executor.execute()
                    dismiss()
                }
                setCanceledOnTouchOutside(true)
                show()
            }

        }
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
fun Fragment.permissionRequest(
    vararg permissions: String,
    deniedStr: String = "You have denied permission. You cannot open this module or use this function",
    rationaleStr: String = "You must agree to this permission to use this module or function. Are you sure you want to agree to this permission?",
    block: () -> Unit
) {
    AndPermission.with(this).runtime()
        .permission(permissions)
        .rationale { _, _, executor ->

            //使用弹窗再次确认
            FangIOSDialog(activity).apply {
                setTitle("Alert")
                setMessage(rationaleStr)
                setNegativeButton("No") {
                    executor.cancel()
                    dismiss()
                }
                setPositiveButton("Yes") {
                    executor.execute()
                    dismiss()
                }
                setCanceledOnTouchOutside(true)
                show()
            }

        }
        .onGranted {
            block()
        }
        .onDenied {
            toast(deniedStr)
        }
        .start()
}