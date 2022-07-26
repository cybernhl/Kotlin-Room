package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar

import android.content.Intent
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo4Binding
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.banner.DemoBannerActivity
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.InterceptChain
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.lai.*
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.popup.DemoXPopupActivity
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toastSuccess
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.StatusBarUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.view.LoadingDialogManager
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


/**
 * 吐司 弹窗 banner
 */
class Demo4Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo4Binding>() {

    lateinit var newMemberIntercept: InterceptNewMember

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo4Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo4)
            .addBindingParams(BR.click, ClickProxy())
    }


    override fun startObserve() {
        LiveEventBus.get("newMember", Boolean::class.java).observe(this) {
            newMemberIntercept.resetNewMember()
        }
    }

    override fun init() {

        //默认的状态栏是白背景-黑文字
        //这里改为随EasyTitle的背景-白色文字
        StatusBarUtils.setStatusBarWhiteText(this)
        StatusBarUtils.immersive(this)

    }


    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun testToast() {
            toastSuccess("Test Tosast")
        }

        fun navPopupPage() {
            DemoXPopupActivity.startInstance()
        }

        fun navBannerPage() {
            DemoBannerActivity.startInstance()
        }

        fun navIntercept() {

            //模拟网络请求
            LoadingDialogManager.get().showLoading(this@Demo4Activity)
            CommUtils.getHandler().postDelayed({
                LoadingDialogManager.get().dismissLoading()

                val bean = JobInterceptBean(true, false, false, false, true, true, true, true, true, true)

                createIntercept(bean)
            }, 1500)


        }

        //创建拦截弹窗
        private fun createIntercept(bean: JobInterceptBean) {
            newMemberIntercept = InterceptNewMember(bean)

            val chain = InterceptChain.create(4)
                .attach(this@Demo4Activity)
                .addInterceptor(newMemberIntercept)
                .addInterceptor(InterceptFillInfo(bean))
                .addInterceptor(InterceptMemberApprove(bean))
                .addInterceptor(InterceptSkill(bean))
                .build()

            //开始执行
            chain.process()

        }


        fun testAsync() {
            testflow()

        }


    }

    private fun testflow() {

//        val bannerFlow = MutableStateFlow<String?>(null)
//        val listFlow = MutableStateFlow<String?>(null)
//
//        lifecycleScope.launch {
//
//            listOf(bannerFlow, listFlow).merge().collect {
//
//                YYLogUtils.w("value:$it")
//            }
//
//        }
//
//        lifecycleScope.launch {
//
//            withContext(Dispatchers.Default) {
//                delay(1000)
//                bannerFlow.emit("Banner")
//            }
//
//            withContext(Dispatchers.Default) {
//                delay(3000)
//                listFlow.emit("list")
//            }
//
//        }


            flow {
                YYLogUtils.w( "start: ${Thread.currentThread().name}")
                repeat(3) {
                    delay(1000)
                    this.emit(it)
                }
                YYLogUtils.w( "end: ${Thread.currentThread().name}")
            }
                .flowOn(Dispatchers.Main)
                .onEach {
                    YYLogUtils.w( "collect: $it, ${Thread.currentThread().name}")
                }
                .launchIn(lifecycleScope)


    }

}