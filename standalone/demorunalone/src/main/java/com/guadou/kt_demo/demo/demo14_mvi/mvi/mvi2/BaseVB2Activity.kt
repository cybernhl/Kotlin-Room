package com.guadou.kt_demo.demo.demo14_mvi.mvi.mvi2

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.guadou.lib_baselib.base.activity.AbsActivity
import com.guadou.lib_baselib.ext.getVMCls

import java.lang.reflect.ParameterizedType

abstract class BaseVB2Activity<VM : ViewModel, VB : ViewBinding> : AbsActivity() {

    protected lateinit var mViewModel: VM

    private var _binding: VB? = null
    protected val mBinding: VB
        get() = requireNotNull(_binding) { "ViewBinding对象为空" }

    //反射创建ViewModel
    open protected fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVMCls(this))
    }

    //反射创建ViewBinding
    open protected fun createViewBinding() {

        val clazz: Class<*> =  (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VB>

        try {
            _binding = clazz.getMethod(
                "inflate", LayoutInflater::class.java
            ).invoke(null, layoutInflater) as VB

        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalArgumentException("无法通过反射创建ViewBinding对象")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        createViewBinding()
        super.onCreate(savedInstanceState)
    }

    override fun setContentView() {
        setContentView(mBinding.root)
        mViewModel = createViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}