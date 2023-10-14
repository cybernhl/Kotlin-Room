package com.guadou.kt_demo.demo.demo18_customview.viewgroup


import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.ToastUtils
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.gotoActivity


class ViewGroup2Activity : BaseVMActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().gotoActivity<ViewGroup2Activity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_demo_viewgroup2

    override fun startObserve() {

    }

    override fun init() {

        findViewById<ViewGroup>(R.id.view_group2).click {
            ToastUtils.makeText(mActivity, "点击回去")
//            Demo18CustomViewActivity.startInstance()

//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("zrjz://routers"))
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("zrjz://main"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

}