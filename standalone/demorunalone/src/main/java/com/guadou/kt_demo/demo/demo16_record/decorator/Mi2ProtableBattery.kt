package com.guadou.kt_demo.demo.demo16_record.decorator

import com.guadou.lib_baselib.utils.log.YYLogUtils

/**
 * 小米充电宝2，实现的是装饰类
 */
class Mi2ProtableBattery(private val portableBattery: PortableBattery) : PortableBatteryDecorator(portableBattery) {

    override fun charge() {
        super.charge()
        //我们就能让充电宝2 一边充电一边照明
        lighting()
    }

    override fun lighting() {
        YYLogUtils.w("充电的时候打开照明灯")
    }

}