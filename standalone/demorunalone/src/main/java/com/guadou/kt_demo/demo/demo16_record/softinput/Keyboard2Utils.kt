package com.guadou.kt_demo.demo.demo16_record.softinput

import android.app.Activity
import android.view.View
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat


object Keyboard2Utils {

    fun addKeyBordHeightChangeCallBack(activity: Activity, onAction: (Int) -> Unit) {

//        activity.window.decorView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
//            override fun onViewAttachedToWindow(v: View?) {
//
//                //开启软键盘动画监听
//                ViewCompat.setWindowInsetsAnimationCallback(activity.window.decorView, object : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {
//
//                    override fun onProgress(insets: WindowInsetsCompat, runningAnimations: MutableList<WindowInsetsAnimationCompat>): WindowInsetsCompat {
//
//                        val imeHeight: Int = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
//                        val navHeight: Int = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
//                        val hasNavigationBar = insets.isVisible(WindowInsetsCompat.Type.navigationBars()) &&
//                                insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom > 0
//
//                        onAction.invoke(if (hasNavigationBar) Math.max(imeHeight - navHeight, 0) else imeHeight)
//
//                        return insets
//                    }
//                })
//
//            }
//
//            override fun onViewDetachedFromWindow(v: View?) {
//
//            }
//        })

        ViewCompat.setOnApplyWindowInsetsListener(activity.window.decorView,object : OnApplyWindowInsetsListener {
            override fun onApplyWindowInsets(v: View?, insets: WindowInsetsCompat): WindowInsetsCompat {

                //开启软键盘动画监听
                ViewCompat.setWindowInsetsAnimationCallback(activity.window.decorView, object : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {

                    override fun onProgress(insets: WindowInsetsCompat, runningAnimations: MutableList<WindowInsetsAnimationCompat>): WindowInsetsCompat {

                        val imeHeight: Int = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                        val navHeight: Int = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                        val hasNavigationBar = insets.isVisible(WindowInsetsCompat.Type.navigationBars()) &&
                                insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom > 0

                        onAction.invoke(if (hasNavigationBar) Math.max(imeHeight - navHeight, 0) else imeHeight)

                        return insets
                    }
                })

                return insets
            }
        })

//        //开启软键盘动画监听
//        ViewCompat.setWindowInsetsAnimationCallback(activity.window.decorView, object : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {
//
//            override fun onProgress(insets: WindowInsetsCompat, runningAnimations: MutableList<WindowInsetsAnimationCompat>): WindowInsetsCompat {
//
//                val imeHeight: Int = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
//                val navHeight: Int = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
//                val hasNavigationBar = insets.isVisible(WindowInsetsCompat.Type.navigationBars()) &&
//                        insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom > 0
//
//                onAction.invoke(if (hasNavigationBar) Math.max(imeHeight - navHeight, 0) else imeHeight)
//
//                return insets
//            }
//        })

    }

}