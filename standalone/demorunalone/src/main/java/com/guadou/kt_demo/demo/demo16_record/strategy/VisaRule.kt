package com.guadou.kt_demo.demo.demo16_record.strategy

class VisaRule : BaseRule<JobCheck> {

    override fun execute(rule: JobCheck): RuleResult {

        return if (rule.visa.lowercase() == "Singapore".lowercase()) {
            RuleResult(true)
        } else {
            RuleResult(false, "签证不满足工作需求")
        }


    }

}