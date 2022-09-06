package com.hongyegroup.receiver

import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.utils.log.YYLogUtils
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ivImg = findViewById<ImageView>(R.id.iv_img)

        findViewById<TextView>(R.id.btn_get).click {
            queryFiles()
        }

    }

    private fun queryFiles() {
//        val uri = Uri.parse("content://com.guadou.kt_demo.fileprovider/external_app_cache/pos/naixiao-1122.jpg")
        val uri = Uri.parse("content://com.guadou.kt_demo.fileprovider/external_app_cache/pos/")

//        val fis = contentResolver.openInputStream(uri)
//        if (fis != null) {
//
//            val inBuffer = fis.source().buffer()
//
//            val outFile = File(getExternalFilesDir(null), "abc")
//            outFile.sink().buffer().use {
//                it.writeAll(inBuffer)
//                inBuffer.close()
//            }
//
//            YYLogUtils.w("保存文件成功")
//
//        }


        val cursor = contentResolver.query(uri, null, null, null, null)

        if (cursor != null) {

            while (cursor.moveToNext()) {

                val fileName = cursor.getString(cursor.getColumnIndex("_display_name"));
                val size = cursor.getLong(cursor.getColumnIndex("_size"));
                val uri = cursor.getString(cursor.getColumnIndex("uri"));

                val fileUri = Uri.parse(uri)

                //就可以使用IO或者BitmapFactory来操作流了

                YYLogUtils.w("name: $fileName  size: $size")
                Toast.makeText(this, "name: $fileName  size: $size", Toast.LENGTH_SHORT).show()
            }

            cursor.close()

        } else {
            YYLogUtils.w("cursor-result: 为空啊")
            Toast.makeText(this, "cursor-result: 为空啊", Toast.LENGTH_SHORT).show()
        }

    }

}