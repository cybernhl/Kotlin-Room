package com.guadou.kt_demo.demo.demo5_network_request

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.lifecycleScope
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo5Binding
import com.guadou.kt_demo.demo.demo5_network_request.mvvm.Demo5ViewModel
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.utils.track.TrackEventListener
import com.jeremyliao.liveeventbus.LiveEventBus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

/**
 * 网络请求的实例代码
 *
 * 一定要注意 Repository和ViewModel 都要在di中注册
 */
@AndroidEntryPoint
class Demo5Activity : BaseVDBActivity<Demo5ViewModel, ActivityDemo5Binding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo5Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo5, BR.viewModel, mViewModel)
            .addBindingParams(BR.click, ClickProxy())
    }


    @SuppressLint("SetTextI18n")
    override fun startObserve() {
        //行业回调
        mViewModel.mIndustryLD.observe(this, {
            mViewModel.mContentLiveData.value = it.toString()

        })

        //学校回调
        mViewModel.mSchoolLD.observe(this, {
            mViewModel.mContentLiveData.value = mBinding.tvNetContent.text.toString() + "\n" + "学校的数据===>：" + "\n"
            mViewModel.mContentLiveData.value = mBinding.tvNetContent.text.toString() + it.toString()
        })

    }

    override fun init() {
        toast("测试-跳转到新页面")
        YYLogUtils.w("ViewModel: $mViewModel Repository:${mViewModel.testRepository()}")
    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun testnull() {
//            mViewModel.testNullNet()
            LiveEventBus.get("newMember").post(true)

//            YYLogUtils.w("Test Log")

            val testNet = TestNet()

            testNet.setOnSuccessCallbackDsl {
                onSuccess { str ->
                    YYLogUtils.w("str: $str")
                    str + "再加一点数据"
                }
                doSth {
                    YYLogUtils.w("可以随便写点什么成功之后的逻辑")
                }
            }

            YYLogUtils.w("no:" + testNet.no)
            "".foo()

            testNet.requestNetwork(onCancel = {

                toast("test network onCancel")

            }, onFailed = {
                //先调用内部的函数处理逻辑
                it.onFailed("哎呦")  //在这里调用内部定义过的函数，如果不调用，TestNet中 YYLogUtils.w("可以随便写点什么逻辑") 不会执行

                it.onError()

                //在打印日志
                YYLogUtils.w("test network onFailed")

            },
//                onSuccess = {
//                //先打印日志
//                YYLogUtils.w("test network onSuccess")
//
//                //再调用内部的函数处理逻辑
//                onSuccess("我的成功数据字符串")    //上面是高阶函数的调用 - 这里是高阶扩展函数的调用，同样的效果，上面需要用it调用，这里直接this 调用
//
//            },
                onFinished = {
                    YYLogUtils.w("当前值是10,满足条件：$it")  //这里的it是那边的回调
                    true  //那边是带参数返回的，这里需要返回Booble给那边
                })

        }

        /**
         * 串联顺序执行
         */
        fun networkChuan() {

            //打印track追踪的网络请求数据
            TrackEventListener.networkTrackCallback = TrackEventListener.NetworkTrackCallback { map -> //可以通过IO写入到文件-上传到服务器
                YYLogUtils.i("track map :$map")
            }

            mViewModel.mContentLiveData.value = ""
            mViewModel.netWorkSeries()
        }

        /**
         * 并发
         */
        fun networkBing() {
            mViewModel.mContentLiveData.value = ""
            mViewModel.netSupervene()
        }

        /**
         * 去重
         */
        fun networkDup() {
            mViewModel.mContentLiveData.value = ""
            //没有防抖动-狂点试试看Log
            mViewModel.netDuplicate()
        }

        //保存到本地文件夹
        @DelicateCoroutinesApi
        fun save2file() {

//            FileIOUtils.writeText(commContext(),"haha.txt",getString(R.string.scroll_content))

//            val result = FileIOUtils.read(commContext(), "haha.txt")
//            YYLogUtils.w("result:$result")

//            val result = FileIOUtils.copyFile2(commContext(), "haha.txt")
//            YYLogUtils.w("result:$result")

//            val result = FileIOUtilKt.writeText(commContext())
//            YYLogUtils.w("result:$result")

            FileIOUtilKt.copyFile()

            //如果不存在就创建一个文件夹
//            val dir = File(dirPath)
//            if (!dir.exists()) {
//                dir.mkdirs()
//            }
//            val dirPath = commContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath
//            val fileName = dirPath + File.separator + "test.txt"
//
//            GlobalScope.launch(Dispatchers.IO) {
//                FileWriter(fileName, true).use {
//                    it.append("测试写入的文本")
//                    it.append("\n")
//                    it.flush()
//                }
//            }

//            extRequestPermission(
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                block = {
//                    //申请权限成功
//                    val directoryPictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//
//                    val fileName = directoryPictures.absolutePath + File.separator + "test.txt"
//                    YYLogUtils.w("外置SD卡路径：" + directoryPictures.absolutePath)
//
//                    GlobalScope.launch(Dispatchers.IO) {
//                        FileWriter(fileName, true).use {
//                            it.append("测试写入的文本")
//                            it.append("\n")
//                            it.flush()
//                        }
//                    }
//
//                })

//            extRequestPermission(
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                block = {
//
//                    val uri = assets.open("naixiao.jpg").use {
//                        it.saveToAlbum(commContext(), fileName = "save_naixiao.jpg", null)
//                    } ?: return@extRequestPermission
//
//                    YYLogUtils.w("保存图片成功：$uri")
//
//                }
//            )

//            extRequestPermission(
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                block = {
//
//                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
//                    startActivityForResult(intent, 1)
//
//                }
//            )


        }

        fun testConcurrency() {

//            for (i in 1..10) {
//                testFun(i)
//            }

            testFun(0)
//            countDown(10, next = { time ->
//                testFun(time)
//            }, start = {}, end = {})

        }
    }

    private fun testFun(index: Int) {
        lifecycleScope.launch {

            val one = async {
                withContext(Dispatchers.Default) {
                    TestUtils.getInstance().value = 1
                    true
                }
            }

            val two = async {
                withContext(Dispatchers.IO) {
                    TestUtils.getInstance().value = 2
                    true
                }

            }

            if (one.await() && two.await()) {
                YYLogUtils.w("index:" + index + " values :" + TestUtils.getInstance().value)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            resultData?.data?.let { uri ->
                YYLogUtils.w("打开文件夹：$uri")
                DocumentFile.fromTreeUri(this, uri)
                    // 在文件夹内创建新文件夹
                    ?.createDirectory("newFolder")
                    ?.apply {
                        // 在新文件夹内创建文件
                        YYLogUtils.w("在新文件夹内创建文件")
                        createFile("text/plain", "test.txt")

                        // 通过文件名找到文件
                        findFile("test.txt")?.also {
                            try {
                                // 在文件中写入内容
                                contentResolver.openOutputStream(it.uri)?.write("hello world".toByteArray())
                                YYLogUtils.w("在文件中写入内容完成")
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        // 删除文件
//                            ?.delete()
                    }
                // 删除文件夹
//                    ?.delete()

            }

        }
        super.onActivityResult(requestCode, resultCode, resultData)
    }

}