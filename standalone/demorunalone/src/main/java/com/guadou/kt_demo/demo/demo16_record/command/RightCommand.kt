package com.guadou.kt_demo.demo.demo16_record.command


class RightCommand(private val tv: Television) : Command() {

    override fun execute() {
        tv.rightAction()
    }

}