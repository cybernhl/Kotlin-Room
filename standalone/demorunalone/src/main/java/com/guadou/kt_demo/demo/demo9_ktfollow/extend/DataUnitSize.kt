package com.guadou.kt_demo.demo.demo9_ktfollow.extend

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToLong

enum class DataUnit(val shortName: String) {
    BYTES("B"),
    KILOBYTES("KB"),
    MEGABYTES("MB"),
    GIGABYTES("GB"),
    TERABYTES("TB"),
}

private const val BYTES_PER_KB: Long = 1024  //KB
private const val BYTES_PER_MB = BYTES_PER_KB * 1024  //MB
private const val BYTES_PER_GB = BYTES_PER_MB * 1024  //GB
private const val BYTES_PER_TB = BYTES_PER_GB * 1024  //TB

//主要的单位转换方法之一
fun convertDataUnit(value: Long, sourceUnit: DataUnit, targetUnit: DataUnit): Long {
    val valueInBytes = when (sourceUnit) {
        DataUnit.BYTES -> value
        DataUnit.KILOBYTES -> Math.multiplyExact(value, BYTES_PER_KB)
        DataUnit.MEGABYTES -> Math.multiplyExact(value, BYTES_PER_MB)
        DataUnit.GIGABYTES -> Math.multiplyExact(value, BYTES_PER_GB)
        DataUnit.TERABYTES -> Math.multiplyExact(value, BYTES_PER_TB)
    }
    return when (targetUnit) {
        DataUnit.BYTES -> valueInBytes
        DataUnit.KILOBYTES -> valueInBytes / BYTES_PER_KB
        DataUnit.MEGABYTES -> valueInBytes / BYTES_PER_MB
        DataUnit.GIGABYTES -> valueInBytes / BYTES_PER_GB
        DataUnit.TERABYTES -> valueInBytes / BYTES_PER_TB
    }
}

//主要的单位转换方法之一
fun convertDataUnit(value: Double, sourceUnit: DataUnit, targetUnit: DataUnit): Double {
    val valueInBytes = when (sourceUnit) {
        DataUnit.BYTES -> value
        DataUnit.KILOBYTES -> value * BYTES_PER_KB
        DataUnit.MEGABYTES -> value * BYTES_PER_MB
        DataUnit.GIGABYTES -> value * BYTES_PER_GB
        DataUnit.TERABYTES -> value * BYTES_PER_TB
    }
    require(!valueInBytes.isNaN()) { "DataUnit value cannot be NaN." }
    return when (targetUnit) {
        DataUnit.BYTES -> valueInBytes
        DataUnit.KILOBYTES -> valueInBytes / BYTES_PER_KB
        DataUnit.MEGABYTES -> valueInBytes / BYTES_PER_MB
        DataUnit.GIGABYTES -> valueInBytes / BYTES_PER_GB
        DataUnit.TERABYTES -> valueInBytes / BYTES_PER_TB
    }
}


@JvmInline
value class DataUnitSize constructor(val rawBytes: Long) {

    fun toDouble(unit: DataUnit): Double = convertDataUnit(rawBytes.toDouble(), DataUnit.BYTES, unit)

    fun toLong(unit: DataUnit): Long = convertDataUnit(rawBytes, DataUnit.BYTES, unit)


    override fun toString(): String = String.format("%dB", rawBytes)

    fun toString(unit: DataUnit, decimals: Int = 2): String {
        require(decimals >= 0) { "小数点必须大于0, 现在的小数点是$decimals" }
        val number = toDouble(unit)
        if (number.isInfinite()) {
            return number.toString()
        }

        val newDecimals = decimals.coerceAtMost(12)
        return DecimalFormat("0").run {
            if (newDecimals > 0) minimumFractionDigits = newDecimals
            roundingMode = RoundingMode.HALF_UP
            format(number) + unit.shortName
        }
    }
}


//定义常用的一系列扩展属性(只读)
val Long.b get() = this.toDataSize(DataUnit.BYTES)
val Long.kb get() = this.toDataSize(DataUnit.KILOBYTES)
val Long.mb get() = this.toDataSize(DataUnit.MEGABYTES)
val Long.gb get() = this.toDataSize(DataUnit.GIGABYTES)
val Long.tb get() = this.toDataSize(DataUnit.TERABYTES)

val Int.b get() = this.toLong().toDataSize(DataUnit.BYTES)
val Int.kb get() = this.toLong().toDataSize(DataUnit.KILOBYTES)
val Int.mb get() = this.toLong().toDataSize(DataUnit.MEGABYTES)
val Int.gb get() = this.toLong().toDataSize(DataUnit.GIGABYTES)
val Int.tb get() = this.toLong().toDataSize(DataUnit.TERABYTES)

val Double.b get() = this.toDataSize(DataUnit.BYTES)
val Double.kb get() = this.toDataSize(DataUnit.KILOBYTES)
val Double.mb get() = this.toDataSize(DataUnit.MEGABYTES)
val Double.gb get() = this.toDataSize(DataUnit.GIGABYTES)
val Double.tb get() = this.toDataSize(DataUnit.TERABYTES)

//定义的扩展方法之一，转换为 new 出来的 DataSize 对象
fun Long.toDataSize(unit: DataUnit): DataUnitSize {
    return DataUnitSize(convertDataUnit(this, unit, DataUnit.BYTES))
}

//定义的扩展方法之一，转换为 new 出来的 DataSize 对象
fun Double.toDataSize(unit: DataUnit): DataUnitSize {
    return DataUnitSize(convertDataUnit(this, unit, DataUnit.BYTES).roundToLong())
}

// 给自定义的 DataSize 定义扩展方法，可以自动格式化到指定的单位
fun DataUnitSize.autoFormatDataSize(): String {
    val dataSize = this
    return when {
        dataSize.inWholeTerabytes >= 1 -> dataSize.toString(DataUnit.TERABYTES)
        dataSize.inWholeGigabytes >= 1 -> dataSize.toString(DataUnit.GIGABYTES)
        dataSize.inWholeMegabytes >= 1 -> dataSize.toString(DataUnit.MEGABYTES)
        else -> dataSize.toString(DataUnit.KILOBYTES)
    }
}

// 给自定义的 DataSize 定义扩展方法，是否满足当前单位
val DataUnitSize.inWholeBytes: Long get() = toLong(DataUnit.BYTES)
val DataUnitSize.inWholeKilobytes: Long get() = toLong(DataUnit.KILOBYTES)
val DataUnitSize.inWholeMegabytes: Long get() = toLong(DataUnit.MEGABYTES)
val DataUnitSize.inWholeGigabytes: Long get() = toLong(DataUnit.GIGABYTES)
val DataUnitSize.inWholeTerabytes: Long get() = toLong(DataUnit.TERABYTES)

