package com.guadou.kt_demo.demo.demo16_record.strategy

class AgeRule : BaseRule<JobCheck> {

    override fun execute(rule: JobCheck): RuleResult {

        return if (rule.age > 16 && rule.age < 50) {
            RuleResult(true)
        } else {
            RuleResult(false, "年龄不满足")
        }

    }

}