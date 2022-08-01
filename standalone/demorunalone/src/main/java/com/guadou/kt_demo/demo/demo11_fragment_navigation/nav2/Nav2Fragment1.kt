package com.guadou.kt_demo.demo.demo11_fragment_navigation.nav2

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.guadou.kt_demo.R
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.databinding.Nav2Fragment1Binding
import com.guadou.lib_baselib.base.fragment.BaseVDBFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.addTextChangedListenerDsl
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.utils.log.YYLogUtils


class Nav2Fragment1 : BaseVDBFragment<EmptyViewModel, Nav2Fragment1Binding>() {

    private val activityViewModel by activityViewModels<Nav2ViewModel>()

    override fun getDataBindingConfig(): DataBindingConfig {

        return DataBindingConfig(R.layout.nav2_fragment1, BR.viewModel, activityViewModel)
    }

    override fun startObserve() {

    }

    override fun init() {

    }

    override fun initViews(view: View) {
        view.findViewById<TextView>(R.id.btn_gotoPage2).click {
            Navigation.findNavController(getView() ?: view).navigate(R.id.action_page2, null)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        YYLogUtils.w("Nav2Fragment1-onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        YYLogUtils.w("Nav2Fragment1-onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        YYLogUtils.w("Nav2Fragment1-onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        YYLogUtils.w("Nav2Fragment1-onDestroy")
        super.onDestroy()
    }

}