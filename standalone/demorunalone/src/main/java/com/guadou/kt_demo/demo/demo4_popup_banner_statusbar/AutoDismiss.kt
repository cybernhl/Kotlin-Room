package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar

import androidx.lifecycle.*
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.youth.banner.util.LogUtils

class AutoDismiss : DefaultLifecycleObserver {

    override fun onDestroy(owner: LifecycleOwner) {
        YYLogUtils.w("销毁吧")
    }

}