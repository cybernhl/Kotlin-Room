package com.guadou.kt_demo.demo.demo11_fragment_navigation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.FragmentDemo11Page1Binding
import com.guadou.kt_demo.demo.demo11_fragment_navigation.callback.IOneActivityCallback
import com.guadou.kt_demo.demo.demo11_fragment_navigation.callback.IOneFragmentCallback
import com.guadou.kt_demo.demo.demo11_fragment_navigation.nav2.DemoNav2Activity
import com.guadou.kt_demo.demo.demo11_fragment_navigation.vm.Demo11ViewModel
import com.guadou.lib_baselib.base.fragment.BaseVDBFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.bus.FlowBus
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.utils.navigation.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//@AndroidEntryPoint
class Demo11OneFragment1(private val test: String) : BaseVDBFragment<EmptyViewModel, FragmentDemo11Page1Binding>(),
    IOnBackPressed, IOneActivityCallback {

//    @Inject
//    lateinit var mMsg: String

//    @Inject
//    lateinit var mMsgLD: MutableLiveData<String>

    private val activityViewModel: Demo11ViewModel by activityViewModels()

    val callback: (Int, String) -> Unit = { int, str ->
        toast("int : $int ; str: $str")
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_demo11_page1)
            .addBindingParams(BR.click, ClickProxy())
    }

    private lateinit var mCallback: IOneFragmentCallback
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mCallback = context as IOneFragmentCallback
    }

    override fun startObserve() {
        //通过Activity中的LiveData来接收返回的数据
        activityViewModel.mBackOneLD.observe(this) {
            YYLogUtils.w("收到LD消息 - $it")
        }

        lifecycleScope.launch {
            activityViewModel.mMsgFlow.collect {
                YYLogUtils.w("收到Flow消息 - $it")
            }
        }



        FlowBus.with<String>("test-key-01").register(this) {
            YYLogUtils.w("收到FlowBus消息 - $it")
        }
    }

    private var firstTime: Long = 0

    private fun finishCurActivity() {
        if (System.currentTimeMillis() - firstTime > 2000) {
            toast("再按一次,退出该页面")
            firstTime = System.currentTimeMillis()
        } else {
            finishActivity()
        }
    }

    override fun init() {
        toast(test)
//        YYLogUtils.w("mMsgLD:$mMsgLD")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        YYLogUtils.w("Page1 - onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onResume() {
        super.onResume()
        YYLogUtils.w("Page1 - onResume")

    }

    override fun onPause() {
        super.onPause()
        YYLogUtils.w("Page1 - onPause")
    }

    override fun onStart() {
        super.onStart()
        YYLogUtils.w("Page1 - onStart")
    }

    override fun onStop() {
        super.onStop()
        YYLogUtils.w("Page1 - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        YYLogUtils.w("Page1 - onDestroy")
    }

    inner class ClickProxy {

        fun nav2Page2() {
            //跳转的几种方式，跳转Class文件
//            navigator.start(
//                Demo11OneFragment2::class,
//                arguments = bundleOf("name" to "zhangsan", "age" to "18"),
//            ) {
//                applySlideInOut()
//            }

            FlowBus.withStick<String>("test-key-02").post(lifecycleScope, "Test Stick Message")

            //方式二
            navigator.start(
                //启动选项
                {
                    applySlideInOut()
                    launchMode = LaunchMode.STANDARD
                },
                //参数
                arguments = bundleOf("name" to "zhangsan", "age" to "18")
            ) {
                //直接创建实例的的方式
                Demo11OneFragment2(callback)
            }

        }

        fun callback() {
            mCallback.callActOne("message come from one page")
        }

        fun gotoNaV2() {
            DemoNav2Activity.startInstance()
        }

    }

    //返回事件- 不穿透交给自己处理
    override fun onBackPressed(): Boolean {
        finishCurActivity()
        return false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        YYLogUtils.w("Demo1 onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    // =======================  callback =========================

    override fun callOneFragment(string: String) {
        YYLogUtils.w("啊，我在Fragment1中被调用了！msg:$string")
    }

}