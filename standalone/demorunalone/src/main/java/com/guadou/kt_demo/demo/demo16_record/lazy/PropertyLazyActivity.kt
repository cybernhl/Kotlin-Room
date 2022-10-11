package com.guadou.kt_demo.demo.demo16_record.lazy

import android.widget.Button
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.utils.log.YYLogUtils
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


class PropertyLazyActivity : BaseVMActivity<EmptyViewModel>() {

    private lateinit var name: String

    private val age: Int by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {

      Thread({
          Thread.sleep(10000)
      }).run()

        return@lazy 18
    }

    override fun getLayoutIdRes(): Int {
        return R.layout.activity_property_lazy
    }

    override fun startObserve() {

    }

    override fun init() {

        name = "zhang san"

        findViewById<Button>(R.id.btn_load).click {

            if (this::name.isInitialized) {
                YYLogUtils.w("name:$name age:$age")
            }

        }
    }

}