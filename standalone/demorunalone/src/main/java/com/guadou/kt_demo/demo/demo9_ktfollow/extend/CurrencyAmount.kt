package com.guadou.kt_demo.demo.demo9_ktfollow.extend

import java.math.RoundingMode
import java.text.DecimalFormat

enum class CurrencyUnit(val prefix: String, val fullName: String, val exchangeRate: Double) {
    CNY("¥", "CNY", 1.0),
    USD("$", "USD", 8.0),
    SGD("S$", "SGD", 5.0),
    HKD("HK$", "HKD", 0.8),
}

//简单的转换方法
fun convertCurrency(value: Double, sourceUnit: CurrencyUnit, targetUnit: CurrencyUnit): Double {
    val valueInRMB = value * sourceUnit.exchangeRate
    return valueInRMB / targetUnit.exchangeRate
}

/**
使用 @JvmInline value class 优化旨在提高内存和性能效率
使用 @JvmInline value class 必须有且仅有一个属性，主构造函数的参数只能是 val，
并且类不能被继承，生成的字节码会在使用处直接嵌入，而不会引入额外的对象

如果不想使用 @JvmInline value class 用普通的class也能行
 */
@JvmInline
value class CurrencyAmount constructor(private val rawAmount: Double) : Comparable<CurrencyAmount> {

    private fun toDouble(unit: CurrencyUnit): Double = convertCurrency(rawAmount, CurrencyUnit.CNY, unit)

    operator fun unaryMinus(): CurrencyAmount {
        return CurrencyAmount(-this.rawAmount)
    }

    operator fun plus(other: CurrencyAmount): CurrencyAmount {
        return CurrencyAmount(this.rawAmount + other.rawAmount)
    }

    operator fun minus(other: CurrencyAmount): CurrencyAmount {
        return this + (-other) // a - b = a + (-b)
    }

    operator fun times(scale: Int): CurrencyAmount {
        return CurrencyAmount(this.rawAmount * scale)
    }

    operator fun div(scale: Int): CurrencyAmount {
        return CurrencyAmount(this.rawAmount / scale)
    }

    operator fun times(scale: Double): CurrencyAmount {
        return CurrencyAmount(this.rawAmount * scale)
    }

    operator fun div(scale: Double): CurrencyAmount {
        return CurrencyAmount(this.rawAmount / scale)
    }

    override fun compareTo(other: CurrencyAmount): Int {
        return this.rawAmount.compareTo(other.rawAmount)
    }

    override fun toString(): String = String.format("¥ %.2f", rawAmount)

    fun toString(unit: CurrencyUnit, decimals: Int = 2): String {
        require(decimals >= 0) { "小数点必须大于0, 现在的小数点是$decimals" }
        val number = toDouble(unit)
        if (number.isInfinite()) {
            return number.toString()
        }
        val newDecimals = decimals.coerceAtMost(12)
        return DecimalFormat("0").run {
            if (newDecimals > 0) minimumFractionDigits = newDecimals
            roundingMode = RoundingMode.HALF_UP
            unit.prefix + " " + format(number)
        }
    }
}

//定义常用的一系列扩展属性(只读)
val Double.cny get() = CurrencyAmount(this)
val Long.cny get() = CurrencyAmount(this.toDouble())
val Int.cny get() = CurrencyAmount(this.toDouble())

val Double.usd get() = CurrencyAmount(this * CurrencyUnit.USD.exchangeRate)
val Long.usd get() = CurrencyAmount(this.toDouble() * CurrencyUnit.USD.exchangeRate)
val Int.usd get() = CurrencyAmount(this.toDouble() * CurrencyUnit.USD.exchangeRate)

val Double.sgd get() = CurrencyAmount(this * CurrencyUnit.SGD.exchangeRate)
val Long.sgd get() = CurrencyAmount(this.toDouble() * CurrencyUnit.SGD.exchangeRate)
val Int.sgd get() = CurrencyAmount(this.toDouble() * CurrencyUnit.SGD.exchangeRate)

val Double.hkd get() = CurrencyAmount(this * CurrencyUnit.HKD.exchangeRate)
val Long.hkd get() = CurrencyAmount(this.toDouble() * CurrencyUnit.HKD.exchangeRate)
val Int.hkd get() = CurrencyAmount(this.toDouble() * CurrencyUnit.HKD.exchangeRate)

