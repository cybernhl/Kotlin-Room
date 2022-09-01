package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo4Binding
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.banner.DemoBannerActivity
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.InterceptChain
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.lai.*
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.popup.DemoXPopupActivity
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.statusbars.HostImmersiveStatusActivity
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.statusbars.HostNormalStatusActivity
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.statusbars.HostScrollStatusActivity
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.viewmodel.Demo4ViewModel
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toastSuccess
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.StatusBarUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.view.LoadingDialogManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * 吐司 弹窗 banner
 */
@AndroidEntryPoint
class Demo4Activity : BaseVDBActivity<Demo4ViewModel, ActivityDemo4Binding>() {

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


    override fun init() {

        lifecycle.addObserver(AutoDismiss())

        //默认的状态栏是白背景-黑文字
        //这里改为随EasyTitle的背景-白色文字
        StatusBarUtils.setStatusBarWhiteText(this)
        StatusBarUtils.immersive(this)
        //沉浸式之后使用EasyTitleBar的状态栏功能

    }

    private val text = "Hello World."
    private var curIndex = 0
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            if (msg.what == 101) {
                val text: String = msg.obj as String

                YYLogUtils.w("当前展示的字符串：" + text)

                mBinding.tvPrint.text = text

                printForwardMessage(17L)

            } else if (msg.what == 102) {

                val text: String = msg.obj as String

                YYLogUtils.w("当前展示的字符串：" + text)

                mBinding.tvPrint.text = text

                printBackMessage(17L)
            }
        }
    }

    private fun printForwardMessage(delay: Long) {
        val message = handler.obtainMessage()
        message.what = 101
        curIndex += 1
        if (curIndex <= text.length) {
            val text = text.substring(0, curIndex)
            message.obj = text
            handler.sendMessageDelayed(message, delay)
        } else {
            YYLogUtils.w("写完了")
            curIndex = text.length

            printBackMessage(delay)
        }
    }

    private fun printBackMessage(delay: Long) {
        val message = handler.obtainMessage()
        message.what = 102
        curIndex -= 1
        if (curIndex >= 0) {
            val text = text.substring(0, curIndex)
            message.obj = text
            handler.sendMessageDelayed(message, delay)
        } else {
            YYLogUtils.w("写完了")
            curIndex = 0

            printForwardMessage(delay)
        }
    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        val testToast: () -> Unit = {
            toastSuccess("Test Tosast")
        }

        fun testParams(view: View) {
            YYLogUtils.w("view:" + view + " index:" + -1)
        }

        fun navPopupPage() {
            DemoXPopupActivity.startInstance()
        }

        fun navBannerPage() {
            DemoBannerActivity.startInstance()
        }

        //打印文本
        fun printText() {
            printForwardMessage(0)
        }

        //状态栏沉浸式处理 -1
        fun hostNormal() {
            HostNormalStatusActivity.startInstance()
        }

        //状态栏沉浸式处理 -2
        fun hostInvasion() {
            HostImmersiveStatusActivity.startInstance()
        }
        //状态栏沉浸式处理 -3
        fun hostscroll(){
            HostScrollStatusActivity.startInstance()
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


//        fun testAsync(index: Int) {
//            testflow()
//
//        }

        val testAsync: () -> Unit = {
            testflow()
        }


        fun resumeScope() {
            mViewModel.resumeCoroutine()
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


//            flow {
//                YYLogUtils.w( "start: ${Thread.currentThread().name}")
//                repeat(3) {
//                    delay(1000)
//                    this.emit(it)
//                }
//                YYLogUtils.w( "end: ${Thread.currentThread().name}")
//            }
//                .flowOn(Dispatchers.Main)
//                .onEach {
//                    YYLogUtils.w( "collect: $it, ${Thread.currentThread().name}")
//                }
//                .launchIn(lifecycleScope)


//        mViewModel.changeSearch("key")


//        val sharedFlow = flowOf(1, 2, 3).shareIn(
//            scope = lifecycleScope,
////            started = WhileSubscribed(5000, 1000),
////            started = Eagerly,
//            started = Lazily,
//            replay = 0
//        )
//
//
//        lifecycleScope.launch {
//            sharedFlow.collect {
//                YYLogUtils.w("shared-value $it")
//            }
//        }

//        val singleDispatcher = Executors.newSingleThreadExecutor {
//            Thread(it, "SingleThread").apply { isDaemon = true }
//        }.asCoroutineDispatcher()


        lifecycleScope.launch {

            val start = System.currentTimeMillis()
            var count = 0

            suspend fun addActor() = actor<Int>(capacity = Channel.UNLIMITED) {

                for (msg in channel) {
                    when (msg) {
                        0 -> count++
                        1 -> count--
                    }
                }
            }

            val actor = addActor()

            val job1 = CoroutineScope(Dispatchers.IO).launch {
                repeat(99999) {
                    actor.send(0)//加
                }
            }

            val job2 = CoroutineScope(Dispatchers.IO).launch {
                repeat(99999) {
                    actor.send(1)//减
                }
            }

            job1.join()
            job2.join()

            val deferred = CompletableDeferred<Int>()
            deferred.complete(count)
            val result = deferred.await()

            actor.close()

            //等待Job1 Job2执行完毕打印结果
            YYLogUtils.w("count: $result")
            YYLogUtils.w("count:执行耗时：${System.currentTimeMillis() - start}")
        }


//        mViewModel.suspendSth()

//        CommUtils.getHandler().postDelayed({
//            mViewModel.changeSearch("1234")
//        }, 2000)

//
//
//        mViewModel.changeState()

//        mViewModel.getNewsDetail().observe(this) {
//            updateUI()
//        }


//        val executorService: ScheduledExecutorService = Executors.newScheduledThreadPool(3)
//        val command = Runnable {
//            //dosth
//        }
//        executorService.scheduleAtFixedRate(command, 0, 3, TimeUnit.SECONDS)
//
//        executorService.shutdown()
    }


    private fun updateUI() {
        //更新一些UI
    }

    override fun onStart() {
        super.onStart()
        YYLogUtils.w("页面开始onStart了")
    }

    override fun startObserve() {
        lifecycleScope.launch {
            mViewModel.searchFlow.collect {
                YYLogUtils.w("search-state-value $it")
            }
        }

        mViewModel.searchLD.observeForever {
            YYLogUtils.w("search-livedata-value $it")
        }


//        lifecycleScope.launch {
//            mViewModel.sharedFlow.collect {
//                YYLogUtils.w("shared-value1 $it")
//            }
//
//        }
//
//        lifecycleScope.launch {
//            mViewModel.channel.consumeAsFlow().collect {
//                YYLogUtils.w("shared-value2 $it")
//            }
//        }

        lifecycleScope.launch {
            mViewModel.stateFlow.flowWithLifecycle(lifecycle).collect {

            }

//            repeatOnLifecycle(Lifecycle.State.STARTED){
//                mViewModel.stateFlow.collect{
//
//                }
//            }

//            mViewModel.stateFlow.collect {
//                updateUI()
//            }
        }

    }


}

