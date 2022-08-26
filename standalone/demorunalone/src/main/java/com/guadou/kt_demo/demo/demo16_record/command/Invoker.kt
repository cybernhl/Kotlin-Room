package com.guadou.kt_demo.demo.demo16_record.command

/**
 * 调用者
 */
class Invoker(private val command: Command) {

    //调用者发号司令
    fun action(){
        command.execute()
    }

}