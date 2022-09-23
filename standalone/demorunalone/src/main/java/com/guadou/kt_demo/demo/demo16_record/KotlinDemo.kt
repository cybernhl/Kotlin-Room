package com.guadou.kt_demo.demo.demo16_record

import com.guadou.kt_demo.demo.demo5_network_request.TestNet
import com.guadou.kt_demo.demo.demo5_network_request.bean.Industry
import com.guadou.lib_baselib.utils.log.YYLogUtils
import kotlinx.coroutines.*


fun String.setValueCallback0(block: (Int) -> Unit) {
    block(10)
}

fun String.setValueCallback1(block: String.(Int) -> Unit) {
    block(this, 10)
}

fun String.setValueCallback11(block: Industry.(Int) -> Unit) {
    block(Industry(0, this, "123", "456"), 10)
}

fun String.setValueCallback12(block: (Industry, Int) -> Unit) {
    block(Industry(0, this, "123", "456"), 10)
}

fun String.setValueCallback13(block: MyCustomCallback.(String) -> Unit) {

    block(MyCustomCallback { YYLogUtils.w("对方调用了，然后我来继续执行") }, this + "加点后缀")

}

class KotlinDemo {

    companion object {

        var school: String = "wuhandaxue"

        @JvmField
        var industry: String = "IT"

        fun callStaticMethod1() {
            YYLogUtils.w("调用静态方法")
        }

        @JvmStatic
        fun callStaticMethod2() {
            YYLogUtils.w("调用静态方法")
        }
    }

    var name: String = "newki"

    fun printName() {
        YYLogUtils.w("name:$name")
    }

    val age: Int
        get() {
            return 28
        }


    //这种比较常见
    fun String.setValueCallback(block: Industry.() -> Unit) {
        //直接回调给对方
        block(Industry(0, this, "123", "456"))
    }

    //还能设置不相关的对象扩展
    fun setValueCallback2(block: MyCustomCallback.() -> Unit) {

        block(MyCustomCallback { YYLogUtils.w("对方调用了，然后我来继续执行") })

    }

    //协程
    fun setValueCallback3(block: (CoroutineScope) -> Unit) {

        GlobalScope.launch(Dispatchers.IO) {

            block(this)
        }

    }

    fun setValueCallback4(block: suspend CoroutineScope.() -> Unit) {

        GlobalScope.launch(Dispatchers.IO) {


            block.invoke(this)
        }

    }


    fun callLaunch(block: suspend CoroutineScope.() -> Unit) {

        GlobalScope.launch(Dispatchers.Main) {

            block.invoke(this)

        }

    }

    suspend fun callWithContext(block: () -> String): String {

        return withContext(Dispatchers.IO) {

            block()
        }

    }


    fun setSamMethod(callback: MyCustomCallback) {

        callback.onCallback()
    }
}


fun topLevelFun() {
    YYLogUtils.w("调用顶层函数")
}