package com.guadou.kt_demo.demo.demo16_record.decorator

/**
 * 定义一个充电宝的装饰类
 *
 */
abstract class PortableBatteryDecorator(private val portableBattery: PortableBattery) : PortableBattery() {

    //重写父类的方法，这里可以直接调用父类方法，也可以修改之后自行调用
    override fun charge() {
        portableBattery.charge()
    }

    //我们还能通过这样的装饰类扩展他的方法
    abstract fun lighting()

}