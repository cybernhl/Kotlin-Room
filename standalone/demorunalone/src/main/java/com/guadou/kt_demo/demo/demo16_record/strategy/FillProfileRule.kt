package com.guadou.kt_demo.demo.demo16_record.strategy

class FillProfileRule : BaseRule<JobCheck> {

    override fun execute(rule: JobCheck): RuleResult {

        return if (rule.isFillProfile) {
            RuleResult(true)
        } else {
            RuleResult(false, "请完善用户详细信息")
        }

    }

}