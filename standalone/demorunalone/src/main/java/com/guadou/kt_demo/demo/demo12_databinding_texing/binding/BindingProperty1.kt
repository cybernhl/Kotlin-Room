package com.guadou.kt_demo.demo.demo12_databinding_texing.binding

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * 一种封装ViewBinding的方式-并没有用到
 */
private const val TAG = "ViewBindingProperty"

//使用反射     private val binding : FragmentTestBinding by viewBindingV1()
inline fun <reified V : ViewBinding> viewBindingV1() = viewBindingV1(V::class.java)

inline fun <reified T : ViewBinding> viewBindingV1(clazz: Class<T>): FragmentViewBindingPropertyV1<Fragment, T> {
    //反射获取到bind方法去绑定
    val bindMethod = clazz.getMethod("bind", View::class.java)
    return FragmentViewBindingPropertyV1 {
        bindMethod(null, it.requireView()) as T
    }
}

//不使用反射， private val binding by viewBindingV2(FragmentTestBinding::bind)
inline fun <F : Fragment, V : ViewBinding> viewBindingV2(
    crossinline viewBinder: (View) -> V,
    // 类似于创建工厂
    crossinline viewProvider: (F) -> View = Fragment::requireView
) = FragmentViewBindingPropertyV1 { fragment: F ->
    viewBinder(viewProvider(fragment))
}


/**
 * @param viewBinder 创建绑定类对象
 */
class FragmentViewBindingPropertyV1<in F : Fragment, out V : ViewBinding>(
    private val viewBinder: (F) -> V
) : ReadOnlyProperty<F, V> {

    private var viewBinding: V? = null

    @MainThread
    override fun getValue(thisRef: F, property: KProperty<*>): V {
        //如果已经有了直接返回
        viewBinding?.let { return it }

        // Use viewLifecycleOwner.lifecycle other than lifecycle
        val lifecycle = thisRef.viewLifecycleOwner.lifecycle
        val viewBinding = viewBinder(thisRef)
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            Log.w(
                TAG, "Access to viewBinding after Lifecycle is destroyed or hasn't created yet. " +
                        "The instance of viewBinding will be not cached."
            )
        } else {
            lifecycle.addObserver(ClearOnDestroyLifecycleObserver())
            this.viewBinding = viewBinding
        }
        return viewBinding
    }

    @MainThread
    fun clear() {
        viewBinding = null
    }

    private inner class ClearOnDestroyLifecycleObserver : LifecycleObserver {

        private val mainHandler = Handler(Looper.getMainLooper())

        @MainThread
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy(owner: LifecycleOwner) {
            owner.lifecycle.removeObserver(this)
            mainHandler.post { clear() }
        }
    }
}