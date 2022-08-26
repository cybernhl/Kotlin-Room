package com.guadou.kt_demo.demo.demo16_record.command


class TeachEnglishCommand(private val teacher: EnglishTeacher) : Command() {

    override fun execute() {
        teacher.teach()
    }
}