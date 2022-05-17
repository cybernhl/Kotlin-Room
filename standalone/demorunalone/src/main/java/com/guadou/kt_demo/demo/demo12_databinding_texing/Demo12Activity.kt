package com.guadou.kt_demo.demo.demo12_databinding_texing

import android.content.Intent
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo12Binding
import com.guadou.kt_demo.databinding.IncludeDatabindingTestBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * 测试DataBinding的高级特效
 */
class Demo12Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo12Binding>() {

    private val clickProxy: ClickProxy by lazy { ClickProxy() }
//    private val mTestBindingBean: TestBindingBean by lazy { TestBindingBean("第一个文本", "第二个文本", "第三个文本") }

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
//            .addBindingParams(BR.testBean, mTestBindingBean)
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
        val etFlow: MutableStateFlow<String?> = MutableStateFlow(null)

        fun showETText() {
            toast(etLiveData.value)
        }

        fun setData2ET(view: View) {
            etLiveData.value = "设置数据给ET"
            toast("view:${view.toString()}")
        }

        fun testToast() {
            toast("测试吐司")
        }

        //动态的加载布局
        fun inflateXml() {
            //给静态的xml，赋值数据,赋值完成之后 include的布局也可以自动显示
            mBinding.testBean = TestBindingBean("haha", "heihei", "huhu")

            //获取View
            val view = CommUtils.inflate(R.layout.include_databinding_test)
            //绑定DataBinding 并赋值自定义的数据
            DataBindingUtil.bind<IncludeDatabindingTestBinding>(view)?.apply {
                testBean = TestBindingBean("haha1", "heihei1", "huhu1")
                click = clickProxy
            }
            //添加布局
            mBinding.flContent.apply {
                removeAllViews()
                addView(view)
            }
        }

        fun customView() {
            //给静态的xml，赋值数据,赋值完成之后 include的布局也可以自动显示
            mBinding.testBean = TestBindingBean("haha2", "heihei2", "huhu2")

            //动态的添加自定义View
            val customTestView = CustomTestView(mActivity)
            customTestView.setClickProxy(clickProxy)
            customTestView.setTestBean(TestBindingBean("haha3", "heihei3", "huhu3"))

            mBinding.flContent2.apply {
                removeAllViews()
                addView(customTestView)
            }
        }

    }
}