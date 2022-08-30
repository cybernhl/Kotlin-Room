package com.guadou.kt_demo.demo.demo16_record.strategy

class GenderRule(private val requirGender: Int) : BaseRule<JobCheck> {

    override fun execute(rule: JobCheck): RuleResult {

        return if (requirGender == 2) {
            RuleResult(true)
        } else {

            RuleResult(
                rule.gender == requirGender,
                if (rule.gender == requirGender) "" else "性别不符合此工作"
            )

        }

    }

}