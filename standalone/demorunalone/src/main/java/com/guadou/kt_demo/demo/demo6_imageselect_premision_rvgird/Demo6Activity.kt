package com.guadou.kt_demo.demo.demo6_imageselect_premision_rvgird


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.widget.ImageView
import androidx.documentfile.provider.DocumentFile
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo6Binding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.engine.extLoad
import com.guadou.lib_baselib.engine.extRequestPermission
import com.guadou.lib_baselib.engine.image_select.extOpenCamera
import com.guadou.lib_baselib.engine.image_select.extOpenImageSelect
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import okio.buffer
import okio.sink
import okio.source
import java.io.File


/**
 * 相机相册
 */
class Demo6Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo6Binding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo6Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }

        const val DOC = "application/msword"
        const val DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        const val XLS = "application/vnd.ms-excel application/x-excel"
        const val XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        const val PPT = "application/vnd.ms-powerpoint"
        const val PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation"
        const val PDF = "application/pdf"
        const val TXT = "text/plain"
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo6)
            .addBindingParams(BR.click, ClickProxy())
    }

    @SuppressLint("SetTextI18n")
    override fun startObserve() {

    }

    override fun init() {
        initRV()
    }

    private fun initRV() {

        mBinding.recyclerView
            .vertical(3)
            .bindData(mImageSelectDatas, R.layout.item_local_image) { holder, t, _ ->
                holder.getView<ImageView>(R.id.iv_img).extLoad(t.path)
            }
            .addItemDecoration(GridSpacingItemDecoration(3, dp2px(10f), true))
    }

    //已经选择的图片集合
    private var mImageSelectDatas = arrayListOf<LocalMedia>()


    private fun refreshRV() {
        toast("Refresh")
        mBinding.recyclerView.adapter?.notifyDataSetChanged()
    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        /**
         * 相机
         */
        fun selectAlbum() {
            //申请权限
            extRequestPermission(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                block = {

                    //获取到权限成功-打开单选相册
                    extOpenImageSelect(mImageSelectDatas, object : OnResultCallbackListener<LocalMedia> {

                        override fun onResult(result: MutableList<LocalMedia>?) {
                            result?.also {
                                mImageSelectDatas.clear()
                                mImageSelectDatas.addAll(it)

                                refreshRV()
                            }
                        }

                        override fun onCancel() {
                            toast("取消了选择")
                        }

                    }, selectNum = 1, canTackPhoto = true, canCompress = true, canCrop = true, ratioX = 1, ratioY = 1)

                }
            )
        }

        /**
         * 照相机
         */
        fun selectCamera() {
            //申请权限
            extRequestPermission(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                block = {

                    //获取到权限成功-打开单选相册
                    extOpenCamera(mImageSelectDatas, object : OnResultCallbackListener<LocalMedia> {

                        override fun onResult(result: MutableList<LocalMedia>?) {
                            result?.also {
                                mImageSelectDatas.clear()
                                mImageSelectDatas.addAll(it)

                                refreshRV()
                            }
                        }

                        override fun onCancel() {
                            toast("取消了选择")
                        }

                    }, canCompress = true, canCrop = true)

                }
            )
        }

        /**
         * 多选
         */
        fun muiltAlbum() {
            //申请权限
            extRequestPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                block = {

                    //获取到权限成功-打开单选相册
                    extOpenImageSelect(mImageSelectDatas, object : OnResultCallbackListener<LocalMedia> {

                        override fun onResult(result: MutableList<LocalMedia>?) {
                            result?.also {
                                mImageSelectDatas.clear()
                                mImageSelectDatas.addAll(it)

                                refreshRV()
                            }
                        }

                        override fun onCancel() {
                            toast("取消了选择")
                        }

                    }, selectNum = 9, canTackPhoto = false, canCompress = true, canCrop = false, ratioX = 1, ratioY = 1)

                }
            )
        }

        /**
         * 写入文件测试
         */
        fun writeFile() {
            extRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE) {

//                val downLoadPath = Environment.getExternalStoragePublicDirectory("DownloadMyFiles").absolutePath
//                val parent = File(downLoadPath)

//                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
//                startActivityForResult(intent, 1)


                val uri = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3ADownloadMyFiles%2Fabcd")

                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
                intent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                )
                startActivityForResult(intent, 2)

//                }


//                val isDirectory = parent.isDirectory
//                val canRead = parent.canRead()
//                val canWrite = parent.canWrite()
//
//                YYLogUtils.w("${android.os.Build.VERSION.RELEASE} isDirectory：$isDirectory canRead:$canRead canWrite:$canWrite path:$downLoadPath")

                //获取文件流写入文件到File
//                val newFile = File(parent.absolutePath + "/材料清单PDF.pdf")
//                if (!newFile.exists()) {
//                    newFile.createNewFile()
//                }
//
//                val inputStream = assets.open("材料清单PDF.pdf")
//                val inBuffer = inputStream.source().buffer()
//
//                newFile.sink(true).buffer().use {
//                    it.writeAll(inBuffer)
//                    inBuffer.close()
//                }

            }
        }

        /**
         * 测试读取文件的相关操作
         */
        fun readFile() {

            val downLoadPath = Environment.getExternalStoragePublicDirectory("DownloadMyFiles").absolutePath

            val parentFile = File(downLoadPath)

            if (parentFile.exists() && parentFile.isDirectory) {

                val listFiles = parentFile.listFiles()

                if (listFiles != null && listFiles.isNotEmpty()) {

                    val nameList = arrayListOf<String>()

                    listFiles.forEach { file ->

                        if (file.exists()) {

                            if (file.isDirectory) {
                                val fileName = file.name
                                nameList.add(fileName)
                            } else {
                                val fileName = file.name
                                nameList.add(fileName)
                            }

                        }
                    }

                    YYLogUtils.w("${android.os.Build.VERSION.RELEASE} 找到的文件和文件夹为：$nameList")

                }
            }

//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//
//                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//                intent.addCategory(Intent.CATEGORY_OPENABLE)
//
//                //指定选择文本类型的文件
//                intent.type = "*/*"
//                //指定多类型查询
//                intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(PDF, DOC, DOCX, PPT, PPTX, XLS, XLSX, TXT))
//
//
//                startActivityForResult(intent, 10402)
//            }

        }

        /**
         * 选择文件
         */
        @SuppressLint("Recycle")
        fun chooseFile() {

            extRequestPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) {

                val downLoadPath1 = Environment.getExternalStoragePublicDirectory("DownloadMyFiles").absolutePath

                val uri = DocumentsContract.buildChildDocumentsUri(
                    "com.guadou.kt_demo.selectfileprovider.authorities",
                    downLoadPath1
                )

//                val uri = DocumentsContract.buildRootsUri("com.guadou.kt_demo.selectfileprovider.authorities")

                val cursor = contentResolver.query(uri, null, null, null, null)
                YYLogUtils.w("cursor $cursor")

                cursor?.run {

                    while (moveToNext()) {

                        val documentId = getString(getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID))
                        val displayName = getString(getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME))
                        val type = getString(getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE))
                        val flag = getInt(getColumnIndex(DocumentsContract.Document.COLUMN_FLAGS))
                        val size = getLong(getColumnIndex(DocumentsContract.Document.COLUMN_SIZE))
                        val updateAt = getLong(getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED))

                        YYLogUtils.w("${android.os.Build.VERSION.RELEASE} documentId:$documentId displayName:$displayName type:$type flag:$flag " +
                                "size:$size updateAt:$updateAt")
                    }

                    close()
                }

            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {

            resultData?.data?.let { uri ->
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                YYLogUtils.w("打开文件夹：$uri")
                DocumentFile.fromTreeUri(this, uri)
                    // 在文件夹内创建新文件夹
                    ?.createDirectory("DownloadMyFiles")
                    ?.apply {
                        // 在新文件夹内创建文件
                        YYLogUtils.w("在新文件夹内创建文件")
                        createFile("text/plain", "test.txt")

                        // 通过文件名找到文件
                        findFile("test.txt")?.also {
                            try {
                                // 在文件中写入内容
                                contentResolver.openOutputStream(uri)?.write("hello world".toByteArray())
                                YYLogUtils.w("在文件中写入内容完成")
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        // 删除文件
                        // ?.delete()
                    }
                // 删除文件夹
                // ?.delete()

            }

        } else if (requestCode == 2) {
            //单独申请指定文件夹权限
            resultData?.data?.let {
                contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )

                //进行文件写入的操作
                val documentFile: DocumentFile? = DocumentFile.fromTreeUri(mActivity, it)

                YYLogUtils.w("documentFile:" + documentFile + " uri:" + documentFile?.uri)

                documentFile?.run {
                    val findFile = createFile("application/pdf", "材料清单PDF")
                    YYLogUtils.w("findFile:" + findFile + " uri:" + findFile?.uri)

                    findFile?.uri?.let {
                        val outs = contentResolver.openOutputStream(it)
                        val inBuffer = assets.open("材料清单PDF.pdf").source().buffer()
                        outs?.sink()?.buffer()?.use {
                            it.writeAll(inBuffer)
                            inBuffer.close()
                        }
                    }
                }
            }

        }

    }

}