package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment.lazy2

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.fragment.BaseVMLoadingFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.view.AsyncViewStub
import com.guadou.lib_baselib.view.gloading.Gloading
import com.guadou.lib_baselib.view.gloading.GloadingGlobalAdapter
import com.guadou.lib_baselib.view.gloading.GloadingRoatingAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Lazy2Fragment1 : BaseVMLoadingFragment<EmptyViewModel>() {

    private var isInitDataLoaded = false
    private lateinit var mViewStub: AsyncViewStub

    companion object {
        fun obtainFragment(): Lazy2Fragment1 {
            return Lazy2Fragment1()
        }
    }

    //重新生成GLoading对象
    override fun generateGLoading(view: View): Gloading.Holder {
        return Gloading.from(GloadingGlobalAdapter()).wrap(view).withRetry {
            onGoadingRetry()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.lazy2_fragment1


    override fun startObserve() {

    }

    override fun init() {

    }

    override fun initViews(view: View) {
        mViewStub = view.findViewById(R.id.view_stub_1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        YYLogUtils.w("Lazy2Fragment1 - onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        YYLogUtils.w("Lazy2Fragment1 - onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        YYLogUtils.w("Lazy2Fragment1 - onResume")

        if (!isInitDataLoaded) {
            onLazyInitData()
        }
    }

    override fun onGoadingRetry() {
        onLazyInitData()
    }

    private fun onLazyInitData() {
        YYLogUtils.w("Lazy2Fragment1 - initData")

        lifecycleScope.launch {

            //模拟的Loading的情况
            showStateLoading()

            val isInflate = async {
                if (!mViewStub.isInflate()) {
                    mViewStub.inflateAsync()
                }

                true
            }

            val data = async(Dispatchers.IO) {
                delay(1000)
                isInitDataLoaded = true
                "return data"
            }

            if (!TextUtils.isEmpty(data.await()) && isInflate.await()) {
                showStateSuccess()
                //popupData2View
            }

        }

    }
}
