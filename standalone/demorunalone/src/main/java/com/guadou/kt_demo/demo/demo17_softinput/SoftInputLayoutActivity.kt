package com.guadou.kt_demo.demo.demo17_softinput


import android.widget.EditText
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo17_softinput.utils.SoftInputUtil
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel



class SoftInputLayoutActivity : BaseVMActivity<EmptyViewModel>() {

    private val softInputUtil = SoftInputUtil()

    override fun getLayoutIdRes(): Int {
        return R.layout.activity_soft_input_layout
    }

    override fun startObserve() {

    }

    override fun init() {

        val etInput = findViewById<EditText>(R.id.et_input)
        etInput.bringToFront()

        softInputUtil.adjustETWithSoftInput(etInput) { isSoftInputShow, softInputHeight, viewOffset ->

            if (isSoftInputShow) {
                etInput.translationY = etInput.translationY - viewOffset
            } else {
                etInput.translationY = 0f;
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()

        softInputUtil.releaseETWithSoftInput()
    }
}