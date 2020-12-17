package com.guadou.kt_demo.demo.demo11_fragment_navigation

import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseFragment
import com.guadou.lib_baselib.base.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import kotlinx.android.synthetic.main.fragment_demo11_page3.*


class Demo11OneFragment3 : BaseFragment<EmptyViewModel>() {

    companion object {
        fun obtainFragment(): Demo11OneFragment3 {
            return Demo11OneFragment3()
        }
    }

    override fun initVM(): EmptyViewModel {
        val viewModel: EmptyViewModel by viewModels()
        return viewModel
    }

    override fun inflateLayoutById(): Int = R.layout.fragment_demo11_page3


    override fun startObserve() {

    }

    override fun init() {

        btn_to_page2.click {
            Navigation.findNavController(it).navigateUp()
        }

        btn_to_page1.click {
            Navigation.findNavController(it).navigate(R.id.action_page3_to_page1)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        YYLogUtils.w("Page3 - onDestroy")
    }

}