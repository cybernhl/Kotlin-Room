package com.guadou.kt_demo.demo.demo16_record.strategy

interface BaseRule<T> {

    fun execute(rule: T): RuleResult
}