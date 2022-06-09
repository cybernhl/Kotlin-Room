package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class InterceptChain private constructor(
    // 弹窗的时候可能需要Activity/Fragment环境。
    val activity: FragmentActivity? = null,
    val fragment: Fragment? = null,
    private var interceptors: MutableList<Interceptor>?
) {
    companion object {

        @JvmStatic
        fun create(count: Int = 0): Builder {
            return Builder(count)
        }
    }

    private var index: Int = 0

    // 执行拦截器。
    fun process() {
        interceptors ?: return
        when (index) {
            in interceptors!!.indices -> {
                val interceptor = interceptors!![index]
                index++
                interceptor.intercept(this)
            }

            interceptors!!.size -> {
                clearAllInterceptors()
            }
        }
    }

    private fun clearAllInterceptors() {
        interceptors?.clear()
        interceptors = null
    }

    // 构建者模式。
    open class Builder(private val count: Int = 0) {
        private val interceptors by lazy(LazyThreadSafetyMode.NONE) {
            ArrayList<Interceptor>(count)
        }
        private var activity: FragmentActivity? = null
        private var fragment: Fragment? = null

        // 添加一个拦截器。
        fun addInterceptor(interceptor: Interceptor): Builder {
            if (!interceptors.contains(interceptor)) {
                interceptors.add(interceptor)
            }
            return this
        }

        // 关联Fragment。
        fun attach(fragment: Fragment): Builder {
            this.fragment = fragment
            return this
        }

        // 关联Activity。
        fun attach(activity: FragmentActivity): Builder {
            this.activity = activity
            return this
        }


        fun build(): InterceptChain {
            return InterceptChain(activity, fragment, interceptors)
        }
    }
}