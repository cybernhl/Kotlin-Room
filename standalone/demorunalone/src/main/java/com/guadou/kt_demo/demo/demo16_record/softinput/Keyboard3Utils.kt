package com.guadou.kt_demo.demo.demo16_record.softinput

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.guadou.lib_baselib.utils.log.YYLogUtils

object Keyboard3Utils {

    fun addKeyBordHeightChangeCallBack(view: View, onAction: (height: Int) -> Unit) {
        var posBottom: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val cb = object : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
                override fun onProgress(
                    insets: WindowInsets,
                    animations: MutableList<WindowInsetsAnimation>
                ): WindowInsets {
                    posBottom = insets.getInsets(WindowInsets.Type.ime()).bottom +
                            insets.getInsets(WindowInsets.Type.systemBars()).bottom
                    onAction.invoke(posBottom)
                    return insets
                }
            }
            view.setWindowInsetsAnimationCallback(cb)
        } else {
            ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
                posBottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom +
                        insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
                onAction.invoke(posBottom)
                insets
            }
        }
    }


    fun registerKeyboardHeightListener(activity: Activity, listener: KeyboardHeightListener) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            activity.window.decorView.setWindowInsetsAnimationCallback(object : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
                override fun onProgress(insets: WindowInsets, animations: MutableList<WindowInsetsAnimation>): WindowInsets {

                    val imeHeight = insets.getInsets(WindowInsets.Type.ime()).bottom

                    val navigationBarHeight = insets.getInsets(WindowInsets.Type.navigationBars()).bottom

                    val hasNavigationBar = insets.isVisible(WindowInsets.Type.navigationBars()) &&
                            insets.getInsets(WindowInsets.Type.navigationBars()).bottom > 0

                    if (imeHeight > navigationBarHeight && hasNavigationBar) {

                        listener.onKeyboardHeightChanged(imeHeight - navigationBarHeight)

                    } else {
                        listener.onKeyboardHeightChanged(imeHeight)
                    }

                    return insets
                }
            })

        } else {
            ViewCompat.setOnApplyWindowInsetsListener(activity.window.decorView) { _, insets ->

                val posBottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom

                listener.onKeyboardHeightChanged(posBottom)

                insets
            }
        }

    }

    fun interface KeyboardHeightListener {
        fun onKeyboardHeightChanged(height: Int)
    }
}