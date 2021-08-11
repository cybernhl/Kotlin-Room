package com.guadou.kt_demo.demo.demo13_motionlayout

import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext

class Demo13JavaActivity : BaseVMActivity<EmptyViewModel>() {

    private var toggle = true

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo13JavaActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getLayoutIdRes(): Int {
        return R.layout.activity_demo13_java
    }

    override fun startObserve() {
    }

    override fun init() {
        val raw = ConstraintSet().apply {
            this.clone(this@Demo13JavaActivity, R.layout.activity_demo13_java)
        }

        val detail = ConstraintSet().apply {
            this.clone(this@Demo13JavaActivity, R.layout.activity_demo13_java_transform)
        }

        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraint_parent)

        constraintLayout.click {
            val constraintSet = if (toggle) detail else raw
            TransitionManager.beginDelayedTransition(constraintLayout)
            constraintSet.applyTo(constraintLayout)

            toggle = !toggle
        }

//        constraint_parent.setOnToggleListener { toggle ->
//
//        }

    }

}