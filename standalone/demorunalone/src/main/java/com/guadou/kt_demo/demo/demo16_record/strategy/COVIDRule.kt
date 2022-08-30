package com.guadou.kt_demo.demo.demo16_record.strategy

class COVIDRule(private val callback: () -> Unit) : BaseRule<JobCheck> {

    override fun execute(rule: JobCheck): RuleResult {

        return if (rule.hasCovidTest) {
            RuleResult(true)
        } else {
            callback()
            RuleResult(false, "你没有新冠检测报告")
        }
    }

}