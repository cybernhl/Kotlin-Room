package com.guadou.kt_demo.demo.demo13_motionlayout

import android.content.Intent
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo13MotionlayoutBinding
import com.guadou.kt_demo.demo.demo13_motionlayout.bean.DialogPass
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import com.guadou.lib_baselib.utils.interceptor.InterceptChain
import com.guadou.lib_baselib.view.FangIOSDialog

class Demo13MotionActivity : BaseVDBActivity<EmptyViewModel, ActivityDemo13MotionlayoutBinding>() {

    private val clickProxy: ClickProxy by lazy { ClickProxy() }

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo13MotionActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo13_motionlayout)
            .addBindingParams(BR.click, clickProxy)
    }

    override fun startObserve() {
    }

    override fun init() {
        //测试拦截器模式
//        val intercepts = InterceptChainHandler<DialogPass>()
//
//        intercepts.add(OneIntercept())
//        intercepts.add(TwoIntercept())
//        intercepts.add(ThreeIntercept())
//        intercepts.add(FourIntercept())
//        intercepts.add(FiveIntercept())
//
//        val dialogPass = DialogPass().apply {
//            msg = "测试弹窗流程"
//            passType = 1
//        }
//
//        intercepts.intercept(dialogPass)
    }

    inner class OneIntercept : InterceptChain<DialogPass>() {

        override fun intercept(data: DialogPass?) {
            if (data != null) {
                val interceptData = "当前的Data1:${data.msg}"
                YYLogUtils.w(interceptData)

                //判断当前是否弹窗
                if (data.passType == 1) {
                    FangIOSDialog(mActivity).apply {
                        setTitle("测试弹框1")
                        setMessage("弹框的内容")
                        setNegativeButton("No") {
                            dismiss()
                        }
                        setPositiveButton("Yes") {
                            dismiss()
                            //判断逻辑，是否需要弹出下一个弹窗
                            //如果需要弹出下一个弹窗 设置下一个弹窗的passType
                            data.passType = 2
                            super.intercept(data)
                        }
                        show()
                    }
                }else{
                    super.intercept(data)
                }
            } else {
                super.intercept(data)
            }
        }
    }

    inner class TwoIntercept : InterceptChain<DialogPass>() {

        override fun intercept(data: DialogPass?) {
            if (data != null) {
                val interceptData = "当前的Data2:${data.msg}"
                YYLogUtils.w(interceptData)

                //判断当前是否弹窗
                if (data.passType == 2) {
                    FangIOSDialog(mActivity).apply {
                        setTitle("测试弹框2")
                        setMessage("弹框的内容")
                        setNegativeButton("No") {
                            dismiss()
                        }
                        setPositiveButton("Yes") {
                            dismiss()
                            //判断逻辑，是否需要弹出下一个弹窗
                            //如果需要弹出下一个弹窗 设置下一个弹窗的passType
                            data.passType = 3
                            super.intercept(data)
                        }
                        show()
                    }
                }else{
                    super.intercept(data)
                }
            } else {
                super.intercept(data)
            }
        }
    }

    inner class ThreeIntercept : InterceptChain<DialogPass>() {

        override fun intercept(data: DialogPass?) {
            if (data != null) {
                val interceptData = "当前的Data3:${data.msg}"
                YYLogUtils.w(interceptData)

                //判断当前是否弹窗
                if (data.passType == 3) {
                    FangIOSDialog(mActivity).apply {
                        setTitle("测试弹框3")
                        setMessage("弹框的内容")
                        setNegativeButton("No") {
                            dismiss()
                        }
                        setPositiveButton("Yes") {
                            dismiss()
                            //判断逻辑，是否需要弹出下一个弹窗
                            //如果需要弹出下一个弹窗 设置下一个弹窗的passType
                            data.passType = 5
                            super.intercept(data)
                        }
                        show()
                    }
                }else{
                    super.intercept(data)
                }
            } else {
                super.intercept(data)
            }
        }

    }

    inner class FourIntercept : InterceptChain<DialogPass>() {

        override fun intercept(data: DialogPass?) {
            if (data != null) {
                val interceptData = "当前的Data4:${data.msg}"
                YYLogUtils.w(interceptData)

                //判断当前是否弹窗
                if (data.passType == 4) {
                    FangIOSDialog(mActivity).apply {
                        setTitle("测试弹框4")
                        setMessage("弹框的内容")
                        setNegativeButton("No") {
                            dismiss()
                        }
                        setPositiveButton("Yes") {
                            dismiss()
                            //判断逻辑，是否需要弹出下一个弹窗
                            //如果需要弹出下一个弹窗 设置下一个弹窗的passType
                            data.passType = 5
                            super.intercept(data)
                        }
                        show()
                    }
                }else{
                    super.intercept(data)
                }
            } else {
                super.intercept(data)
            }
        }

    }

    inner class FiveIntercept : InterceptChain<DialogPass>() {

        override fun intercept(data: DialogPass?) {
            if (data != null) {
                val interceptData = "当前的Data5:${data.msg}"
                YYLogUtils.w(interceptData)

                //判断当前是否弹窗
                if (data.passType == 5) {
                    FangIOSDialog(mActivity).apply {
                        setTitle("测试弹框5")
                        setMessage("弹框的内容")
                        setNegativeButton("No") {
                            dismiss()
                        }
                        setPositiveButton("Yes") {
                            dismiss()
                            //判断逻辑，是否需要弹出下一个弹窗
                            //如果需要弹出下一个弹窗 设置下一个弹窗的passType
                        }
                        show()
                    }
                }else{
                    super.intercept(data)
                }
            } else {
                super.intercept(data)
            }
        }

    }


    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun gotoJavaContrcol() {
            Demo13JavaActivity.startInstance()
        }

        fun gotoXmlContrcol() {
            Demo13XmlActivity.startInstance()
        }

        fun appbar() {
            Demo13AppbarActivity.startInstance()
        }

        fun viewPager() {
            Demo13ViewPagerActivity.startInstance()
        }

    }

}