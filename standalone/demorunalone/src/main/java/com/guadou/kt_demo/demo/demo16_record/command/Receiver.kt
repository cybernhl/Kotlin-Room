package com.guadou.kt_demo.demo.demo16_record.command

import com.guadou.lib_baselib.utils.log.YYLogUtils

/**
 * 接收者，就是具体干活的人
 */
class Receiver {

    //这里执行的方法可以自己写，也可以通过接口定义，如果是多个接收者的话推荐接口定义
    fun doSth(){
        YYLogUtils.w("干点什么呢？搬个砖吧！")
    }

}