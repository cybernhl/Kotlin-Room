package com.guadou.kt_demo.demo.demo11_fragment_navigation


import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.FragmentDemo11Page2Binding
import com.guadou.lib_baselib.base.fragment.BaseVDBFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.bus.FlowBus
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.utils.navigation.applySlideInOut
import com.guadou.lib_baselib.utils.navigation.navigator
import com.guadou.lib_baselib.utils.navigation.pop
import com.guadou.lib_baselib.utils.navigation.start

class Demo11OneFragment2(private val _callback: ((Int, String) -> Unit)?) : BaseVDBFragment<EmptyViewModel, FragmentDemo11Page2Binding>() {

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_demo11_page2)
            .addBindingParams(BR.click, ClickProxy())
    }

    override fun startObserve() {
        FlowBus.withStick<String>("test-key-02").register(this){
            YYLogUtils.w("收到粘性消息：$it")
        }
    }

    override fun init() {
        val bundleText = arguments?.getString("age")
        toast("age:$bundleText")
    }

    override fun onResume() {
        super.onResume()
        YYLogUtils.w("Page2 - onResume")

    }

    override fun onPause() {
        super.onPause()
        YYLogUtils.w("Page2 - onPause")
    }

    override fun onStart() {
        super.onStart()
        YYLogUtils.w("Page2 - onStart")
    }


    override fun onStop() {
        super.onStop()
        YYLogUtils.w("Page2 - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        YYLogUtils.w("Page2 - onDestroy")
    }

    inner class ClickProxy {

        fun back2Page1() {
            //ViewModel回调
//            val viewModel = getActivityVM(Demo11ViewModel::class.java)
//            viewModel.mBackOneLiveData.value = "返回给One Page的数据"

            //高阶函数回调
            _callback?.invoke(10, "返回给One Page的数据")

            // 主线程-发送消息
            FlowBus.with<String>("test-key-01").post(this@Demo11OneFragment2.lifecycleScope, "Test Flow Bus Message")

            navigator.pop()
        }

        fun nav2Page3() {

//            navigator.start(Demo11OneFragment3::class) {
//                applySlideInOut()
//            }

            navigator.start({ applySlideInOut() }) {
                Demo11OneFragment3(arguments?.getString("name"))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        YYLogUtils.w("Demo2 onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }
}