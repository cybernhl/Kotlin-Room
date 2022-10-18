package com.guadou.kt_demo.demo.demo16_record

import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo16_record.softinput.*
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.utils.log.YYLogUtils


class SoftInputActivity : BaseVMActivity<EmptyViewModel>() {

    private lateinit var keyboardHeightUtils: KeyboardHeightUtils
    private lateinit var mIvVoice: View

    override fun getLayoutIdRes(): Int {
        return R.layout.activity_soft_input
    }

    override fun startObserve() {

    }

    override fun init() {

        mIvVoice = findViewById(R.id.iv_voice)


//        Keyboard1Utils.registerKeyboardHeightListener(this) {
//
//            YYLogUtils.w("当前的软键盘高度：$it")
//
//            updateVoiceIcon(it)
//        }


        keyboardHeightUtils = KeyboardHeightUtils(this)
//        keyboardHeightUtils.registerKeyboardHeightListener {
//            YYLogUtils.w("第二种方式：当前的软键盘高度：$it")
//            updateVoiceIcon(it)
//        }
//        keyboardHeightUtils.start()

//        Keyboard2Utils.addKeyBordHeightChangeCallBack(mActivity) { height ->
//
//            YYLogUtils.w("第二种方式：当前的软键盘高度：$height")
//
//            updateVoiceIcon(height)
//        }

        Keyboard4Utils.registerKeyboardHeightListener(this) {
            YYLogUtils.w("第三种方式：当前的软键盘高度：$it")
            updateVoiceIcon(it)
        }

    }

    override fun onResume() {
        super.onResume()

    }

    //更新语音图标的位置
    private fun updateVoiceIcon(height: Int) {

        mIvVoice.updateLayoutParams<FrameLayout.LayoutParams> {
            bottomMargin = height
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Keyboard1Utils.unregisterKeyboardHeightListener(this)

        keyboardHeightUtils.close();
    }

    fun demo1(view: View) {

        ViewCompat.getWindowInsetsController(view)?.show(WindowInsetsCompat.Type.ime())
    }

    fun demo2(view: View) {
        ViewCompat.getWindowInsetsController(view)?.hide(WindowInsetsCompat.Type.ime())
    }


}