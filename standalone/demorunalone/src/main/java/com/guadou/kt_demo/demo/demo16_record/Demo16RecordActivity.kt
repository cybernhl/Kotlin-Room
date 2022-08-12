package com.guadou.kt_demo.demo.demo16_record

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo16HomeBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.engine.extRequestPermission
import com.guadou.lib_baselib.engine.getUri
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * 录制
 */
@AndroidEntryPoint
class Demo16RecordActivity : BaseVDBActivity<EmptyViewModel, ActivityDemo16HomeBinding>() {

    private val clickProxy: ClickProxy by lazy { ClickProxy() }
    private val scheduledExecutorService: ScheduledExecutorService = Executors.newScheduledThreadPool(3)

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

    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun autoSize() {
            Demo16AutoSizeActivity.startInstance()
        }

        fun intent() {
            YYLogUtils.w("ForegroundCheck isForeground: " + ForegroundCheck.get().isForeground)
        }

        fun mediaRecord() {
            CommUtils.getHandler().postDelayed({
                YYLogUtils.w("ForegroundCheck isForeground: " + ForegroundCheck.get().isForeground)
            }, 3000)
        }

        fun testLocation() {

            extRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION) {

                startService(Intent(mActivity, LocationService::class.java))

            }

        }

        //测试下载
        fun testDownLoad() {

//            extRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) {

                startDownLoad()

//            }
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
        val downloadManager = commContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager

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

                val downloaded = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
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