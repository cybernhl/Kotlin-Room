package com.guadou.kt_demo.demo.demo13_motionlayout.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.material.appbar.AppBarLayout
import com.guadou.lib_baselib.utils.log.YYLogUtils

/**
 * 在Appbar内部使用的MotionLayout 与CoordinatorLayout中RV ViewPager等联动
 */
class MyAppbarMotionLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr), AppBarLayout.OnOffsetChangedListener {

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val progressVal = -verticalOffset / appBarLayout?.totalScrollRange?.toFloat()!!
        YYLogUtils.w("progress:$progressVal")
        progress = progressVal

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        (parent as? AppBarLayout)?.addOnOffsetChangedListener(this)
    }
}
