package com.guadou.kt_demo.demo.demo16_record.command


class TeachChineseCommand(private val teacher: ChineseTeacher) : Command() {

    override fun execute() {
        teacher.teach()
    }
}