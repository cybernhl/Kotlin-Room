package com.guadou.kt_demo.demo.demo16_record.list

import androidx.lifecycle.lifecycleScope
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.utils.log.YYLogUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis


class SequencesActivity : BaseVMActivity<EmptyViewModel>() {

    override fun getLayoutIdRes(): Int {
        return R.layout.activity_property_seqences
    }

    override fun startObserve() {

    }

    override fun init() {

        val list = mutableListOf<Person>()
        for (i in 1..3000) {
            list.add(Person("name$i", (0..100).random()))
        }

        val time = measureTimeMillis {
            val list1 = list.filter {
                it.age > 50
            }.map {
                it.name
            }.take(3)

            YYLogUtils.w("list1$list1")
        }

        YYLogUtils.w("耗费的时间$time")


        val time2 = measureTimeMillis {
            val list2 = list.asSequence()
                .filter {
                    it.age > 50
                }.map {
                    it.name
                }.take(3).toList()

            YYLogUtils.w("list2$list2")
        }

        YYLogUtils.w("耗费的时间2$time2")

        lifecycleScope.safeLaunch<String> {
            onRequest = {
               "1234"   //这里一定要return指定的数据，放最后一行即可
            }

            onSuccess = {

            }

            onError = {

            }
        }
    }

}

fun <T> CoroutineScope.safeLaunch(init: CoroutineBuilder<T>.() -> Unit) {
    val result = CoroutineBuilder<T>().apply { init() }

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        result.onError?.invoke(throwable)
    }

    this.launch(exceptionHandler) {
        val res: T? = result.onRequest?.invoke()
        res?.let {
            result.onSuccess?.invoke(it)
        }
    }
}

class CoroutineBuilder<T> {
    var onRequest: (suspend () -> T)? = null
    var onSuccess: ((T) -> Unit)? = null
    var onError: ((Throwable) -> Unit)? = null
}