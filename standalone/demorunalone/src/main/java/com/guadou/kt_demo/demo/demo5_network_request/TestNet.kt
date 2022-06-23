package com.guadou.kt_demo.demo.demo5_network_request

import com.guadou.lib_baselib.utils.log.YYLogUtils
import kotlinx.coroutines.*
import java.util.*

fun String.foo() {
    YYLogUtils.w("扩展函数 foo1")
}

val TestNet.no: Int get() = 1  //扩展成员变量

fun TestNet.setOnSuccessCallbackDsl(init: SuccessCallbackImpl.() -> Unit){
    val listener = SuccessCallbackImpl()
    init(listener)
    this.setOnSuccessCallback(listener)
}

class TestNet {

    fun String.foo() {
        YYLogUtils.w("扩展函数 foo2")
    }

    fun requestNetwork(
        onCancel: () -> Unit,   //空参和空返回值
        onFinished: ((Boolean) -> Boolean)? = null,  //带参数带返回值
//        onSuccess: SuccessCallback.() -> String,   //使用高阶扩展函数的方式
        onFailed: (FailedCallback) -> Unit     //使用带参数的高阶函数的方式
    ) {

        MainScope().launch {

            "".foo()

            val result = withContext(Dispatchers.IO) {
                delay(1500)

                return@withContext Random().nextInt(10)
            }

            YYLogUtils.w("result:$result")

            when {
                result == 10 -> {
                    val res = onFinished?.invoke(true)

                    YYLogUtils.w("接收到对面return的值 :$res")
                }
                result > 8 -> {
                    onCancel()
                }
                result > 5 -> {
//                    val res = onSuccess(object : SuccessCallback {
//                        override fun onSuccess(str: String): String {
//                            //这里的str是外包传进来的，我们对这个字符串做处理
//                            val str2 = "$str 再加一点数据"
//                            YYLogUtils.w("result:$str2")
//                            return str2
//                        }
//
//                        override fun doSth() {
//                            YYLogUtils.w("可以随便写点什么成功之后的逻辑")
//                        }
//                    })
//
//                    YYLogUtils.w("res:$res")


                   val res =  callback?.onSuccess("success")

                    YYLogUtils.w("res:$res")

                    callback?.doSth()

                }
                else -> {
                    onFailed(object : FailedCallback {    //这种接口的方式只能使用object的实现了
                        override fun onFailed(str: String) {
                            YYLogUtils.w("可以随便写点什么Failed逻辑 :$str")
                        }

                        override fun onError() {
                            YYLogUtils.w("可以随便写点什么Error逻辑")
                        }
                    })
                }
            }
        }

    }

    interface SuccessCallback {  //多个参数不能使用fun修饰了
        fun onSuccess(str: String): String

        fun doSth()
    }

    interface FailedCallback {   //多个参数不能使用fun修饰了
        fun onFailed(str: String)

        fun onError()
    }

    var callback: SuccessCallback? = null

    fun setOnSuccessCallback(callback: SuccessCallback) {
        this.callback = callback
    }
}