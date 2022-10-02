package com.guadou.kt_demo.demo.demo16_record.viewpager

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.guadou.lib_baselib.base.fragment.BaseVMFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.kt_demo.R

internal class VPItemChildFragment(private var index: Int) : BaseVMFragment<EmptyViewModel>() {

    override fun getLayoutIdRes(): Int {
        return R.layout.fragment_vp_item_child
    }

    override fun startObserve() {}

    override fun init() {

    }

    @SuppressLint("SetTextI18n")
    override fun initViews(view: View) {

        view.findViewById<TextView>(R.id.tv_item_text).text = "我是第 ${index + 1} 个Item"
    }


}