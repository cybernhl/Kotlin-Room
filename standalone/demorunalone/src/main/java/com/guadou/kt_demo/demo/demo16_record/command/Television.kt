package com.guadou.kt_demo.demo.demo16_record.command

import com.guadou.lib_baselib.utils.log.YYLogUtils


class Television {

    fun leftAction() {
        YYLogUtils.w("电视上的往左选中")
    }

    fun rightAction() {
        YYLogUtils.w("电视上的往右选中")
    }

    fun upAction() {
        YYLogUtils.w("电视上的往上选中")
    }

    fun downAction() {
        YYLogUtils.w("电视上的往下选中")
    }
}