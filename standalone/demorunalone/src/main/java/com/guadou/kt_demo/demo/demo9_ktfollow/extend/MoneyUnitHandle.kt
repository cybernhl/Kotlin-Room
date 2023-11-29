//package com.guadou.kt_demo.demo.demo9_ktfollow.extend
//
//enum class CurrencyUnit(val shortName: String, val toRMBRate: Double) {
//    RMB("RMB", 1.0),
//    USD("USD", 8.0),
//    SGD("SGD", 5.0),
//    HKD("HKD", 0.8)
//}
//
//
//fun convertCurrency(value: Double, sourceUnit: CurrencyUnit, targetUnit: CurrencyUnit): Double {
//    val valueInRMB = value * sourceUnit.toRMBRate
//    return valueInRMB / targetUnit.toRMBRate
//}
//
//@JvmInline
//value class CurrencyAmount(private val amount: Double) : Comparable<CurrencyAmount> {
//
//    private fun toUnit(unit: CurrencyUnit): Double = convertCurrency(amount, CurrencyUnit.RMB, unit)
//
//    operator fun unaryMinus(): CurrencyAmount = CurrencyAmount(-this.amount)
//
//    operator fun plus(other: CurrencyAmount): CurrencyAmount = CurrencyAmount(this.amount + other.amount)
//
//    operator fun minus(other: CurrencyAmount): CurrencyAmount = this + (-other) // a - b = a + (-b)
//
//    operator fun times(scale: Double): CurrencyAmount = CurrencyAmount(this.amount * scale)
//
//    operator fun div(scale: Double): CurrencyAmount = CurrencyAmount(this.amount / scale)
//
//    override fun compareTo(other: CurrencyAmount): Int = this.amount.compareTo(other.amount)
//
//    override fun toString(): String = String.format("%.2f RMB", amount)
//
//    fun toString(unit: CurrencyUnit): String {
//        val convertedAmount = toUnit(unit)
//        return String.format("%.2f %s", convertedAmount, unit.shortName)
//    }
//}
//
//// 定义常用的扩展属性
//val Double.rmb get() = CurrencyAmount(this)
//val Double.usd get() = CurrencyAmount(this * CurrencyUnit.USD.toRMBRate)
//val Double.sgd get() = CurrencyAmount(this * CurrencyUnit.SGD.toRMBRate)
//val Double.hkd get() = CurrencyAmount(this * CurrencyUnit.HKD.toRMBRate)