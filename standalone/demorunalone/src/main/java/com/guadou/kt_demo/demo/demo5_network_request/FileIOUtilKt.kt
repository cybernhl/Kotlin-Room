package com.guadou.kt_demo.demo.demo5_network_request

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.guadou.lib_baselib.ext.commContext
import okio.*
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import kotlin.io.use

/**
 *
 * //读取该文件的所有内容作为一个字符串返回
public fun File.readText(charset: Charset = Charsets.UTF_8): String = readBytes().toString(charset)
//读取文件的每一行内容，存入一个List返回
public fun File.readLines(charset: Charset = Charsets.UTF_8): List<String>
//读取文件的所有内容以ByteArray的方式返回
public fun File.readBytes(): ByteArray = inputStream().use { input ->
//覆盖写入ByteArray字节数组流
public fun File.writeBytes(array: ByteArray): Unit = FileOutputStream(this).use { it.write(array) }
//覆盖写入text字符串到文件中
public fun File.writeText(text: String, charset: Charset = Charsets.UTF_8): Unit = writeBytes(text.toByteArray(charset))
//在文件末尾追加写入text字符串
public fun File.appendText(text: String, charset: Charset = Charsets.UTF_8): Unit = appendBytes(text.toByteArray(charset))
//在文件末尾追加写入ByteArray字节数组流
public fun File.appendBytes(array: ByteArray): Unit = FileOutputStream(this, true).use { it.write(array) }
 *
 *
 */
object FileIOUtilKt {

    //读取文本
    fun readText(context: Context, fileName: String): String {
        val filePath: String = context.filesDir.absolutePath + "/" + fileName
        val file = File(filePath)

//
//
//        file.reader().useLines {  }
//        file.bufferedReader().use {  }
//        file.bufferedWriter()
//        file.inputStream().use {  }
//        file.outputStream().use {  }

        return file.readText()
    }

    fun writeText(context: Context) {

        val newFile = File(context.getExternalFilesDir("copy4"), "copy4.txt")

        FileWriter(newFile).use {
            it.append("writer something...")
            it.append("\n")
            it.flush()
        }

    }


    fun okioWrite(context: Context, fileName: String) {
        val filePath: String = context.filesDir.absolutePath + "/" + fileName
        val file = File(context.filesDir.absolutePath + "/" + fileName)

        //先转换为Sink 在转换为BufferedSink
        file.sink(true).buffer().use {
            it.writeUtf8("writer something..")
            it.writeUtf8("\n")
        }
    }

    val file = File(commContext().filesDir.absolutePath + "/haha.txt")
    private var bufferedSink: BufferedSink? = null
    private var mHandle = Handler(Looper.getMainLooper()) { msg ->
        val sink = checkSink()
        if (msg.what == 1) {
            //写完，flush
            sink.use {
                it.flush()
                bufferedSink = null
            }
        } else {
            //持续写入
            sink.writeUtf8("writer something..")
            sink.writeUtf8("\n")
        }
        false
    }

    fun okioWrite2() {
        mHandle.run {
            removeMessages(1)
            mHandle.sendEmptyMessage(0)
            mHandle.sendEmptyMessageDelayed(1, 2000)
        }
    }

    private fun checkSink(): BufferedSink {
        if (bufferedSink == null) {
            bufferedSink = file.appendingSink().buffer()
        }
        return bufferedSink!!
    }


    fun copyFile() {
        val fis: FileInputStream = commContext().openFileInput(commContext().filesDir.absolutePath + "/haha.txt")
        val inBuffer = fis.source().buffer()

        val outFile = File(commContext().getExternalFilesDir("copy5"), "copy5.txt")
        outFile.sink(true).buffer().use {
            it.writeAll(inBuffer)
            inBuffer.close()
        }

    }


}