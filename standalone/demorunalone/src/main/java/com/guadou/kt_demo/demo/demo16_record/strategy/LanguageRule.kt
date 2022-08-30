package com.guadou.kt_demo.demo.demo16_record.strategy

class LanguageRule(private val requirLanguages: List<String>) :BaseRule<JobCheck> {

    override fun execute(rule: JobCheck): RuleResult {

        return if (requirLanguages.contains(rule.language)) {
            RuleResult(true)
        } else {
            RuleResult(false, "语言不符合此工作")
        }

    }

}