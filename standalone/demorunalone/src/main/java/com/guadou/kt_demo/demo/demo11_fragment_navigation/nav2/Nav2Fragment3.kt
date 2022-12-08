package com.guadou.kt_demo.demo.demo11_fragment_navigation.nav2


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.Nav2Fragment2Binding
import com.guadou.kt_demo.databinding.Nav2Fragment3Binding
import com.guadou.lib_baselib.base.fragment.BaseVDBFragment
import com.guadou.lib_baselib.base.fragment.BaseVMFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.utils.log.YYLogUtils


class Nav2Fragment3 : BaseVDBFragment<EmptyViewModel, Nav2Fragment3Binding>() {

    private val activityViewModel by activityViewModels<Nav2ViewModel>()
    private val viewModel by viewModels<EmptyViewModel>()

    override fun getDataBindingConfig(): DataBindingConfig {

        return DataBindingConfig(R.layout.nav2_fragment3, BR.viewModel, activityViewModel)
    }

    override fun startObserve() {

    }

    override fun init() {

    }

    override fun initViews(view: View) {

        view.findViewById<TextView>(R.id.btn_gotoPage2).click {
            Navigation.findNavController(getView() ?: view).navigateUp()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        YYLogUtils.w("Nav2Fragment3-onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        YYLogUtils.w("Nav2Fragment3-onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        YYLogUtils.w("Nav2Fragment3-onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        YYLogUtils.w("Nav2Fragment3-onDestroy")
        super.onDestroy()
    }

}