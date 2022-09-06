package com.hongyegroup.receiver

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.guadou.lib_baselib.engine.extLoad
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.log.YYLogUtils
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.io.InputStream

class ReceiveImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive_image)

        if (intent != null && intent.action == Intent.ACTION_VIEW) {

            val uri = intent.data
            YYLogUtils.w("uri: $uri")

            if (uri != null && uri.scheme != null && uri.scheme == "content") {

                val fis = contentResolver.openInputStream(uri)

                if (fis != null) {

                    val inBuffer = fis.source().buffer()

                    val outFile = File(getExternalFilesDir("xiaoxiao"), "naixiao5566.jpg")
                    outFile.sink(true).buffer().use {
                        it.writeAll(inBuffer)
                        inBuffer.close()
                    }

                    YYLogUtils.w("存放的路径：${outFile.absolutePath}")

                    //展示
                    val ivReveiverShow = findViewById<ImageView>(R.id.iv_reveiver_show)
                    ivReveiverShow.extLoad(outFile.absolutePath)


//                    val bitmap = BitmapFactory.decodeStream(fis)
//                    //展示
//                    if (bitmap != null) {
//                        val ivReveiverShow = findViewById<ImageView>(R.id.iv_reveiver_show)
//                        ivReveiverShow.setImageBitmap(bitmap)
//                    }

                }
            }
        }
    }

}