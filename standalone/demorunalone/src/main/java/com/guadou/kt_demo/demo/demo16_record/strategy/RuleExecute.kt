package com.guadou.kt_demo.demo.demo16_record.strategy

class RuleExecute<T>(private val check: T, private val hashMap: MutableMap<String, List<BaseRule<T>>>) {

    companion object {

        @JvmStatic
        fun <T> create(check: T): Builder<T> {
            return Builder(check)
        }

        private const val AND = "&&"
        private const val OR = "||"
    }


    // 构建者模式。
    open class Builder<T>(private val check: T) {

        private val hashMap: MutableMap<String, List<BaseRule<T>>> = HashMap()
        var indexSuffix = 0

        //并且的逻辑
        fun and(ruleList: List<BaseRule<T>>): Builder<T> {
            val key = AND + (indexSuffix++).toString()
            hashMap[key] = ruleList
            return this
        }

        //或者的逻辑
        fun or(ruleList: List<BaseRule<T>>): Builder<T> {
            val key = OR + (indexSuffix++).toString()
            hashMap[key] = ruleList
            return this
        }


        fun build(): RuleExecute<T> {
            return RuleExecute(check, hashMap)
        }

    }

    //执行任务
    fun execute(): RuleResult {
        for ((key, ruleList) in hashMap) {
            when (key.substring(0, 2)) {
                AND -> {
                    val result = and(check, ruleList)
                    if (!result.success) {
                        return result
                    }
                }
                OR -> {
                    val result = or(check, ruleList)
                    if (!result.success) {
                        return result
                    }
                }
            }
        }
        return RuleResult(true)
    }

    private fun and(check: T, ruleList: List<BaseRule<T>>): RuleResult {
        for (rule in ruleList) {
            val execute = rule.execute(check)
            if (!execute.success) {
                //失败一次就要return
                return execute
            }
        }
        // 全部匹配成功，返回 true
        return RuleResult(true)
    }

    private fun or(check: T, ruleList: List<BaseRule<T>>): RuleResult {
        val stringBuilder = StringBuilder()

        for (rule in ruleList) {
            val execute = rule.execute(check)
            if (execute.success) {
                // 从前往后遍历，只要一个满足条件就return
                return RuleResult(true)
            } else {
                stringBuilder.append(execute.message)
            }
        }

        // 一个都匹配不到,才返回false
        val result = RuleResult(false, stringBuilder.toString())
        stringBuilder.clear()
        return result
    }

}