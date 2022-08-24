package com.guadou.kt_demo.demo.demo16_record.decorator

/**
 * 定义一个充电宝的基本逻辑，充电
 *
 * 这里可以用接口，可以用类，可以用抽象 都是可以的
 * 只是不同的定义方法，可以默认实现一些重写方法，更加方便而已
 */
abstract class PortableBattery {

    abstract fun charge()

}