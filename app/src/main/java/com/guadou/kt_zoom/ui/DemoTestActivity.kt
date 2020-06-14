package com.guadou.kt_zoom.ui

import android.content.Intent
import android.graphics.Color
import com.google.android.material.tabs.TabLayout
import com.guadou.kt_zoom.R
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.bindFragment
import com.guadou.lib_baselib.utils.CommUtils
import kotlinx.android.synthetic.main.activity_fragment.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class DemoTestActivity : BaseActivity<BaseViewModel>() {

    companion object {
        fun startInstance() {
            val content = CommUtils.getContext()
            content.startActivity(Intent(content, DemoTestActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    override fun initVM(): BaseViewModel = getViewModel()

    override fun startObserve() {

    }

    override fun inflateLayoutById(): Int = R.layout.activity_fragment

    override fun init() {

        viewPager.bindFragment(
            supportFragmentManager,
            listOf(Demo1Fragment(), Demo2Fragment()),
            listOf("Demo1", "Demo2")
        )

        tabLayout.setupWithViewPager(viewPager)
    }


}
