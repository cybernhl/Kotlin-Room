package com.guadou.kt_demo.demo.demo16_record.strategy

class NationalityRule(private val requirNationalitys: List<String>) : BaseRule<JobCheck> {

    override fun execute(rule: JobCheck): RuleResult {

        return if (requirNationalitys.contains(rule.nationality)) {
            RuleResult(true)
        } else {
            RuleResult(false, "国籍不符合此工作")
        }

    }

}