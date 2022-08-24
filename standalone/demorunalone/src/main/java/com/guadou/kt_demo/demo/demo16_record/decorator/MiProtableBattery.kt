package com.guadou.kt_demo.demo.demo16_record.decorator

import com.guadou.lib_baselib.utils.log.YYLogUtils

/**
 * 小米充电宝，默认的实现就是可以充电的
 */
class MiProtableBattery : PortableBattery() {

    override fun charge() {
        YYLogUtils.w("使用小米充电宝充电")
    }

}