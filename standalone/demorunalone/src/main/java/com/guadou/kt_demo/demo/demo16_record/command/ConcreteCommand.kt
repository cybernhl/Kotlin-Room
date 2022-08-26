package com.guadou.kt_demo.demo.demo16_record.command

/**
 * 具体的命令-去干什么事情
 */
class ConcreteCommand(private val receiver: Receiver) : Command() {
    //我们需要一个具体干活的人，就是Receiver

    override fun execute() {
        receiver.doSth()
    }

}