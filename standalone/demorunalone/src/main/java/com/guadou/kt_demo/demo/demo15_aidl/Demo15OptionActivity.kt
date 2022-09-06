package com.guadou.kt_demo.demo.demo15_aidl

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo15MainBinding
import com.guadou.kt_demo.demo.demo15_aidl.provider.MyFileProvider
import com.guadou.kt_demo.demo.demo15_aidl.service1.Service1Activity
import com.guadou.kt_demo.demo.demo15_aidl.service2.Service2Activity
import com.guadou.kt_demo.demo.demo15_aidl.service3.Service3Activity
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.drawable
import com.guadou.lib_baselib.utils.FilesUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

/**
 * AIDL选择
 */
@AndroidEntryPoint
class Demo15OptionActivity : BaseVDBActivity<EmptyViewModel, ActivityDemo15MainBinding>() {

    private val clickProxy: ClickProxy by lazy { ClickProxy() }

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo15OptionActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo15_main)
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

        fun service1() {
            Service1Activity.startInstance()
        }

        fun service2() {
            Service2Activity.startInstance()
        }

        fun service3() {
            Service3Activity.startInstance()
        }

        //测试FileProvider
        fun fileProvider1() {

//            val drawable = drawable(R.drawable.chengxiao)
//            val bd: BitmapDrawable = drawable as BitmapDrawable
//            val bitmap = bd.bitmap
//            FilesUtils.getInstance().saveBitmap(bitmap, "naixiao-1122.jpg")
//
//            val filePath = FilesUtils.getInstance().sdpath + "naixiao-1122.jpg"
//
//            YYLogUtils.w("文件原始路径：$filePath")
//
//            val uri = MyFileProvider.getUriForFile(commContext(), "com.guadou.kt_demo.fileprovider", File(filePath))
//
//            YYLogUtils.w("打印Uri:$uri")
//
//            //到系统中找打开对应的文件
//            openFile(filePath, uri)


            val cursor = contentResolver.query(
                Uri.parse("content://com.guadou.kt_demo.shared.fileprovider/pos"),
//                Uri.parse("content://com.guadou.kt_demo.fileprovider/external_app_cache/pos/"),
                null, null, null, null
            )

            YYLogUtils.w("cursor-result: $cursor")
        }

    }

    private fun openFile(path: String, uri: Uri) {
        //取得文件扩展名
        val extension: String = path.substring(path.lastIndexOf(".") + 1)

        //通过扩展名找到mimeType
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        YYLogUtils.w("mimeType: $mimeType")

        try {
            //构造Intent，启动意图，交由系统处理
            startActivity(Intent().apply {
                //临时赋予读写权限
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                //表示用其它应用打开
                action = Intent.ACTION_VIEW
                //给Intent赋值
                setDataAndType(uri, mimeType)
            })
        } catch (e: Exception) {
            e.printStackTrace()
            YYLogUtils.e("不能打开这种类型的文件")
        }
    }

}