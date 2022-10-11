package com.guadou.kt_demo.demo.demo16_record

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.WindowInsets
import android.widget.Toast
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo16HomeBinding
import com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt.Demo10Activity
import com.guadou.kt_demo.demo.demo16_record.command.*
import com.guadou.kt_demo.demo.demo16_record.decorator.Mi2ProtableBattery
import com.guadou.kt_demo.demo.demo16_record.decorator.MiProtableBattery
import com.guadou.kt_demo.demo.demo16_record.lazy.PropertyLazyActivity
import com.guadou.kt_demo.demo.demo16_record.prototype.Address
import com.guadou.kt_demo.demo.demo16_record.prototype.Company
import com.guadou.kt_demo.demo.demo16_record.strategy.*
import com.guadou.kt_demo.demo.demo16_record.viewpager.ViewPagerNestActivity
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.engine.extRequestPermission
import com.guadou.lib_baselib.ext.color
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.gotoActivity
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.utils.result.ISAFLauncher
import com.guadou.lib_baselib.utils.result.SAFLauncher
import com.guadou.lib_baselib.utils.statusBarHost.StatusBarHost
import com.guadou.lib_baselib.utils.statusBarHost.StatusBarHostLayout
import com.guadou.lib_baselib.utils.statusBarHost.StatusBarHostUtils
import com.guadou.lib_baselib.view.FangIOSDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


/**
 * 其他
 */
@AndroidEntryPoint
class Demo16RecordActivity : BaseVDBActivity<EmptyViewModel, ActivityDemo16HomeBinding>(),
    ISAFLauncher by SAFLauncher() {

    lateinit var hostLayout: StatusBarHostLayout
    private val clickProxy: ClickProxy by lazy { ClickProxy() }
    private val scheduledExecutorService: ScheduledExecutorService = Executors.newScheduledThreadPool(3)

//    //SAF
//    private val requestDataLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == RESULT_OK) {
//            val data = result.data?.getStringExtra("text")
//            toast("拿到返回数据：$data")
//        }
//    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

    }

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo16RecordActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo16_home)
            .addBindingParams(BR.click, clickProxy)
    }

    @SuppressLint("SetTextI18n")
    override fun startObserve() {
    }

    override fun init() {
        initLauncher()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.setOnApplyWindowInsetsListener { view: View, windowInsets: WindowInsets ->

                //导航栏
                val statusBars = windowInsets.getInsets(WindowInsets.Type.statusBars())

                //导航栏高度
                val navigationHeight = Math.abs(statusBars.bottom - statusBars.top)

                YYLogUtils.w("navigationHeight:$navigationHeight")

//                //导航栏
//                val navigationBars = windowInsets.getInsets(WindowInsets.Type.navigationBars())
//                //键盘
//                val ime = windowInsets.getInsets(WindowInsets.Type.ime())

                windowInsets
            }


//            val windowInsets = window.decorView.rootWindowInsets
//            //状态栏
//            val statusBars = windowInsets.getInsets(WindowInsets.Type.statusBars())
//            //状态栏高度
//            val statusBarHeight = statusBars.bottom
//
//            YYLogUtils.w("statusBarHeight2:$statusBarHeight")

            window.decorView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(view: View?) {
                    val windowInsets = window.decorView.rootWindowInsets
                    //状态栏
                    val navigationBars = windowInsets.getInsets(WindowInsets.Type.navigationBars())
                    //状态栏高度
                    val navigationHeight = Math.abs(navigationBars.bottom - navigationBars.top)

                    YYLogUtils.w("navigationHeight:$navigationHeight")
                }

                override fun onViewDetachedFromWindow(view: View?) {
                }
            })
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            //打开键盘
//            window?.insetsController?.apply {

//                show(WindowInsetsCompat.Type.ime())
//
//                show(WindowInsetsCompat.Type.statusBars())
//
//                show(WindowInsetsCompat.Type.navigationBars())
//
//                show(WindowInsetsCompat.Type.systemBars())

//            }
//            window.decorView.windowInsetsController?.show(WindowInsets.Type.ime())


//            window.decorView.setWindowInsetsAnimationCallback(object : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
//                override fun onProgress(insets: WindowInsets, runningAnimations: MutableList<WindowInsetsAnimation>): WindowInsets {
//
//                    val isVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
//                    val keyboardHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
//
//                    //当前是否展示
//                    YYLogUtils.w("isVisible = $isVisible")
//                    //当前的高度进度回调
//                    YYLogUtils.w("keyboardHeight = $keyboardHeight")
//
//                    return insets
//                }
//            })
//
//        }


//        ViewCompat.setWindowInsetsAnimationCallback(window.decorView, object : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {
//            override fun onProgress(insets: WindowInsetsCompat, runningAnimations: MutableList<WindowInsetsAnimationCompat>): WindowInsetsCompat {
//
//                val isVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
//                val keyboardHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
//
//                //当前是否展示
//                YYLogUtils.w("isVisible = $isVisible")
//                //当前的高度进度回调
//                YYLogUtils.w("keyboardHeight = $keyboardHeight")
//
//                return insets
//            }
//        })
//        ViewCompat.getWindowInsetsController(findViewById(android.R.id.content))?.apply {
//
//            show(WindowInsetsCompat.Type.ime())
//
//        }


//        StatusBarHostUtils.getStatusBarHeight(mActivity) {
//            YYLogUtils.w("获取顶部状态栏的高度statusBarsHeight: $it")
//
//        }
//
//        StatusBarHostUtils.getNavigationBarHeight(mActivity) {
//            YYLogUtils.w("获取底部导航栏的高度：" + it)
//        }
//
//        StatusBarHostUtils.hasNavigationBars(mActivity) {
//            YYLogUtils.w("当前页面是否有导航栏：" + it)
//        }
        }

        hostLayout = StatusBarHost.inject(this)
            .setStatusBarBackground(color(R.color.white))
            .setStatusBarBlackText()
            .setNavigationBarBackground(color(R.color.normal_navigation_color))

        val num = 123

        val num1 = kotlin.run {
            YYLogUtils.w("num :$num")
            return@run num + 1
        }
        YYLogUtils.w("num1 :$num1")





        num.apply {
            toString().trim()
        }

        num.also { value ->
            value.toString().trim()
        }

        val num4 = num.let {
            it.toString().trim()
            return@let "222"
        }
        YYLogUtils.w("num4 :$num4")

        val num5 = with(num) {
            toString().trim()
            return@with 111
        }
        YYLogUtils.w("num5 :$num5")
    }


    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun autoSize() {

//            Demo16AutoSizeActivity.startInstance()
            hostLayout.setNavigatiopnBarIconBlack()
//            StatusBarHostUtils.showHideNavigationBar(mActivity, true)
//            StatusBarHostUtils.setStatusBarDarkFont(mActivity, true)
//            StatusBarHostUtils.showHideStatusBar(mActivity, true)
        }

        fun intent() {
//            YYLogUtils.w("ForegroundCheck isForeground: " + ForegroundCheck.get().isForeground)
            hostLayout.setNavigatiopnBarIconWhite()
//            StatusBarHostUtils.showHideNavigationBar(mActivity, false)
//            StatusBarHostUtils.setStatusBarDarkFont(mActivity, false)
//            StatusBarHostUtils.showHideStatusBar(mActivity, false)
        }

        fun mediaRecord() {
//            val testNet = TestNet()
//
//            testNet.setOnSuccessCallbackDsl {
//                onSuccess { str ->
//                    YYLogUtils.w("str: $str")
//                    str + "再加一点数据"
//                }
//                doSth {
//                    YYLogUtils.w("可以随便写点什么成功之后的逻辑")
//                }
//            }
//
//            testNet.requestDSLCallback()

            "test".setValueCallback1 { int ->
                YYLogUtils.w("收到回调：str:" + this + " int:" + int)
            }

            "test".setValueCallback11 { int ->
                YYLogUtils.w("收到回调：industry:" + this.toString() + " int:" + int)
            }

            "test".setValueCallback12 { industry, int ->
                YYLogUtils.w("收到回调：industry:" + industry.toString() + " int:" + int)
            }

            "test".setValueCallback13 { str ->
                YYLogUtils.w("收到回调：callback:" + this + " str:" + str)
                this.onCallback()
            }

            KotlinDemo().setValueCallback3 {

                YYLogUtils.w("直接运行在协程里面")

            }

//            CommUtils.getHandler().postDelayed({
//                YYLogUtils.w("ForegroundCheck isForeground: " + ForegroundCheck.get().isForeground)
//            }, 3000)
        }

        fun testLocation() {

            extRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION) {

                startService(Intent(mActivity, LocationService::class.java))

            }

        }

        //测试下载
        fun testDownLoad() {

            extRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) {

                startDownLoad()

            }
        }

        //装饰者模式
        fun decoratorTest() {
            val mMiProtableBattery = MiProtableBattery()
            mMiProtableBattery.charge()

            val mMi2ProtableBattery = Mi2ProtableBattery(mMiProtableBattery)
            mMi2ProtableBattery.charge()

        }

        //原型模式
        fun prototypeTest() {
//            val userProfile = UserProfile("1", "张三", "30")
//            val skills = arrayListOf("篮球", "游泳", "长跑", "Java")
//            userProfile.address = UserAddress("武汉", "楚河汉街")
//            userProfile.skills = skills
//            YYLogUtils.w(
//                "userProfile:$userProfile  name:" + userProfile.name + " age:" + userProfile.age +
//                        " skill:" + userProfile.skills + "address:" + userProfile.address + " address-city:" + userProfile.address.city
//            )
//
//            try {
//
//                val newUser = userProfile.clone()
//                newUser.name = "李四"
//                newUser.skills.add("H5")
//                newUser.address.city = "长沙"
//                YYLogUtils.w(
//                    "userProfile:$newUser  name:" + newUser.name + " age:" + newUser.age +
//                            " skill:" + newUser.skills + "address:" + newUser.address + " address-city:" + newUser.address.city
//                )
//
//                YYLogUtils.w(
//                    "userProfile:$userProfile  name:" + userProfile.name + " age:" + userProfile.age +
//                            " skill:" + userProfile.skills + "address:" + userProfile.address + " address-city:" + userProfile.address.city
//                )
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }


            val company = Company("百度", "2001年", listOf(Address("北京", "海淀区"), Address("武汉", "江夏区")))

            YYLogUtils.w("company:$company address:${company.addesses}")

            val newCompany = company.copy()

            newCompany.name = "网易"
            newCompany.addesses.get(0).city = "杭州"
            newCompany.addesses.get(0).address = "西湖区"
            YYLogUtils.w("newCompany:$newCompany  address:${newCompany.addesses}")
            YYLogUtils.w("company:$company  address:${company.addesses}")
        }

        //命令模式
        fun commandTest() {
//            val receiver = Receiver()
//            val command = ConcreteCommand(receiver)
//            val invoker = Invoker(command)
//            invoker.action()

//            val chineseTeacher = ChineseTeacher()
//            val englishTeacher = EnglishTeacher()
//
//            val chineseCommand = TeachChineseCommand(chineseTeacher)
//            val englishCommand = TeachEnglishCommand(englishTeacher)
//
//            Invoker(chineseCommand).action()
//            Invoker(englishCommand).action()


            val television = Television()
            val leftCommand = LeftCommand(television)
            val rightCommand = RightCommand(television)
            val upCommand = UpCommand(television)
            val downCommand = DwonCommand(television)

            //传入不同的命令就可以实现不同的逻辑
            Invoker(leftCommand).action()
            Invoker(rightCommand).action()
            Invoker(upCommand).action()
            Invoker(downCommand).action()


        }

        //AFR
        fun resultTest() {

//            gotoActivityForResult<Demo10Activity>(bundle = arrayOf("id" to "123", "name" to "zhangsan")) {
//                val text = it?.getStringExtra("text")
//                toast("拿到返回数据：$text")
//            }


            getLauncher()?.launch(Intent(mActivity, Demo10Activity::class.java)) { result ->
                val data = result.data?.getStringExtra("text")
                toast("拿到返回数据：$data")
            }

//            safLauncher?.launch<Demo10Activity>() { result ->
//                val data = result.data?.getStringExtra("text")
//                toast("拿到返回数据：$data")
//            }

        }

        //軟鍵盤高度
        fun heightSoftInput() {
            gotoActivity<SoftInputActivity>()
        }

        //Java调用Kotlin
        fun javaCallKotlin() {
            gotoActivity<JavaCallKTActivity>()
        }

        //测试ViewPager
        fun testViewPager() {
            gotoActivity<ViewPagerNestActivity>()
        }

        //属性的延时加载
        fun textLazy(){
            gotoActivity<PropertyLazyActivity>()
        }

        //策略模式实战 and or
        fun strategyTest() {
            val jobCheck = JobCheck(
                "S9876543A", "Singapore", "Singapore", 20F,
                3.5F, false, false,
                "Chinese", 1, "25", true,
                false
            )

            jobCheck.hasCovidTest = true
            val covidRule = COVIDRule {
                //如果不满足新冠，首先就被排除了
                //如果没有新冠 - 弹窗提示他
                FangIOSDialog(mActivity).apply {
                    setTitle("你没有新冠表单")
                    setMessage("老哥你快去做核酸吧,老哥你快去做核酸吧,老哥你快去做核酸吧")
                    setPositiveButton("好的") {
                        dismiss()
                    }
                    setNegativeButton("就不去") {
                        dismiss()
                    }
                    show()
                }

            }  //false
            val ageRule = AgeRule()  //true
            val fillProfileRule = FillProfileRule()  //true
            val genderRule = GenderRule(0)  //true
            val languageRule = LanguageRule(listOf("Chinese", "English"))   //true
            val nationalityRule = NationalityRule(listOf("China", "Malaysia"))   //true
            val visaRule = VisaRule()   //true

            //构建规则执行器
            val result = RuleExecute.create(jobCheck)
                .and(listOf(covidRule))
                .or(listOf(nationalityRule, visaRule))
                .and(listOf(ageRule, fillProfileRule, genderRule, languageRule))
                .build()
                .execute()

            YYLogUtils.w("执行规则器的结果：$result")
            toast("执行规则器的结果：$result")
        }
    }

    private fun startDownLoad() {

        //下载链接 这里下载手机B站为示例
        val downloadUrl = "https://dl.hdslb.com/mobile/latest/iBiliPlayer-html5_app_bili.apk"

        val fileName = downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1)
        //这里下载到指定的目录，我们存在公共目录下的download文件夹下
        val fileUri = Uri.fromFile(
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                System.currentTimeMillis().toString() + "-" + fileName
            )
        )
        //开始构建 DownloadRequest 对象
        val request = DownloadManager.Request(Uri.parse(downloadUrl))

        //构建通知栏样式
//        request.setTitle("测试下载标题")
//        request.setDescription("测试下载的内容文本")

        //下载或下载完成的时候显示通知栏
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE or DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        //下载时候隐藏通知栏
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)

        //指定下载的文件类型为APK
        request.setMimeType("application/vnd.android.package-archive")
//            request.addRequestHeader()   //还能加入请求头
//            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)   //能指定下载的网络

        //指定下载到本地的路径(可以指定URI)
        request.setDestinationUri(fileUri)
        //也可以直接指定公共目录
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName)

        //开始构建 DownloadManager 对象
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        Toast.makeText(mActivity, "hello world", Toast.LENGTH_SHORT).show()

        //加入Request到系统下载队列，在条件满足时会自动开始下载。返回的为下载任务的唯一ID
        val requestID = downloadManager.enqueue(request)

        //注册获取进度的监听
        YYLogUtils.w("开始下载：fileUri:$fileUri requestID:$requestID")
        //每秒定时刷新一次
        val command = Runnable {
            getBytesAndStatus(requestID)
        }
        scheduledExecutorService.scheduleAtFixedRate(command, 0, 1, TimeUnit.SECONDS)

        //注册下载任务完成的监听
        commContext().registerReceiver(object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {

                //已经完成
                if (intent.action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

                    //解绑进度监听
                    scheduledExecutorService.shutdown()

                    //获取下载ID
                    val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    val uri = downloadManager.getUriForDownloadedFile(id)
                    YYLogUtils.w("下载完成了- uri:$uri")

                    installApk(uri)

                } else if (intent.action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {

                    //如果还未完成下载，跳转到下载中心
                    YYLogUtils.w("跳转到下载中心")
                    val viewDownloadIntent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
                    viewDownloadIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(viewDownloadIntent)

                }

            }
        }, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    //获取当前进度，和总进度
    private fun getBytesAndStatus(downloadId: Long) {

        val query = DownloadManager.Query().setFilterById(downloadId)
        var cursor: Cursor? = null

        val downloadManager = commContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        try {
            cursor = downloadManager.query(query)
            if (cursor != null && cursor.moveToFirst()) {

//                //Notification 标题
//                val title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))

//                //描述
//                val description = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION))

                val downloaded =
                    cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                val total = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                val progress = downloaded * 100 / total

                YYLogUtils.w("当前下载大小：$downloaded 总共大小：$total")
            }
        } finally {
            cursor?.close()
        }

    }

    private fun installApk(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        startActivity(intent)
    }


}