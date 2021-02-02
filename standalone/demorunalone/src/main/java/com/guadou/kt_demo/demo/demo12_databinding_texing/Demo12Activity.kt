package com.guadou.kt_demo.demo.demo12_databinding_texing

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo12Binding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast

/**
 * 测试DataBinding的高级特效
 */
class Demo12Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo12Binding>() {

    private val clickProxy: ClickProxy by lazy { ClickProxy() }
    private val mTestBindingBean: TestBindingBean by lazy { TestBindingBean("第一个文本", "第二个文本", "第三个文本") }
    private val mTestToastBean: TestToastBean by lazy { TestToastBean("1", "吐司内容") }

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo12Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo12)
            .addBindingParams(BR.click, clickProxy)
            .addBindingParams(BR.testBean, mTestBindingBean)
    }

    override fun startObserve() {

    }

    override fun init() {

    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        val etLiveData: MutableLiveData<String> = MutableLiveData()

        fun showETText() {
            toast(etLiveData.value)
        }

        fun setData2ET() {
            etLiveData.value = "设置数据给ET"
        }

        fun testToast() {
            toast("测试吐司")
        }

        fun inflateXml() {

        }

    }
}