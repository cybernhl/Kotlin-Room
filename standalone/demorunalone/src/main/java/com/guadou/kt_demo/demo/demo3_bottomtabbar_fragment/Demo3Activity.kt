package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo3Binding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.log.YYLogUtils

/**
 * 一般App的首页参考这个类
 */
class Demo3Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo3Binding>() {

    private var mCurPosition: Int = 0
    private var isHomeActDestroy: Boolean = false
    private lateinit var mDemo3OneFragment: Demo3OneFragment
    private lateinit var mDemo3TwoFragment: Demo3TwoFragment
    private lateinit var mDemo3ThreeFragment: Demo3ThreeFragment
    private lateinit var mDemo3FourFragment: Demo3FourFragment

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        YYLogUtils.w("收到newintent:" + intent.toString())
    }

    companion object {

        const val EXTRA_SAVE_INDEX = "extra_save_index"
        const val EXTRA_IS_HOME_ACT_DESTROY = "extra_is_home_act_destroy"

        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo3Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo3)
            .addBindingParams(BR.click, ClickProxy())
    }


    override fun startObserve() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 正常情况下初始化
        if (savedInstanceState == null) {
            initFragment()

            //第一次初始化首页默认显示第一个fragment
            switchFragment(0)

        } else {

            // Activity因为内存不足的时候，这里进行Fragment恢复
            restoreFragmentInstance(savedInstanceState)

            switchFragment(mCurPosition)
        }
    }

    override fun init() {

        initListener()
    }

    private fun initListener() {

    }

    //初始化默认的Fragment
    private fun initFragment() {
        mDemo3OneFragment = Demo3OneFragment.obtainFragment()
        mDemo3TwoFragment = Demo3TwoFragment.obtainFragment()
        mDemo3ThreeFragment = Demo3ThreeFragment.obtainFragment()
        mDemo3FourFragment = Demo3FourFragment.obtainFragment()
    }

    /**
     * 采用 add show 方式切换fragment
     */
    private fun switchFragment(index: Int) {

        val targetFragment = when (index) {
            3 -> {
                mDemo3FourFragment
            }
            2 -> {
                mDemo3ThreeFragment
            }
            1 -> {
                mDemo3TwoFragment
            }
            else -> {
                mDemo3OneFragment
            }
        }

        targetFragment.also {

            val transaction = supportFragmentManager.beginTransaction()
            // 先隐藏掉所有的Fragment
            hideAllFragment(transaction)

            // 在 show 操作
            if (!targetFragment.isAdded) {
                transaction.add(R.id.fl_container, targetFragment, targetFragment.javaClass.simpleName)
                transaction.show(targetFragment)
                transaction.commit()
            } else {
                transaction.show(targetFragment).commit()
            }
            mCurPosition = index
            setBottomSelected(index)
        }

    }

    // 隐藏 fragment
    private fun hideAllFragment(transaction: FragmentTransaction) {
        transaction.hide(mDemo3OneFragment)
        transaction.hide(mDemo3TwoFragment)
        transaction.hide(mDemo3ThreeFragment)
        transaction.hide(mDemo3FourFragment)
    }

    //底部Tab的选中效果
    private fun setBottomSelected(position: Int) {
        //传递进来的Position设置为选中：
        when (position) {
            3 -> {
                mBinding.tabMainOne.isSelected = false
                mBinding.tabMainTwo.isSelected = false
                mBinding.tabMainThree.isSelected = false
                mBinding.tabMainFour.isSelected = true
            }
            2 -> {
                mBinding.tabMainOne.isSelected = false
                mBinding.tabMainTwo.isSelected = false
                mBinding.tabMainThree.isSelected = true
                mBinding.tabMainFour.isSelected = false
            }
            1 -> {
                mBinding.tabMainOne.isSelected = false
                mBinding.tabMainTwo.isSelected = true
                mBinding.tabMainThree.isSelected = false
                mBinding.tabMainFour.isSelected = false
            }
            else -> {
                mBinding.tabMainOne.isSelected = true
                mBinding.tabMainTwo.isSelected = false
                mBinding.tabMainThree.isSelected = false
                mBinding.tabMainFour.isSelected = false
            }
        }
    }

    // 恢复fragment状态
    private fun restoreFragmentInstance(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mCurPosition = savedInstanceState.getInt(EXTRA_SAVE_INDEX, 0)
            isHomeActDestroy = savedInstanceState.getBoolean(EXTRA_IS_HOME_ACT_DESTROY, false)
            val manager = supportFragmentManager
            val f0 = manager.findFragmentByTag(Demo3OneFragment::class.java.simpleName)
            val f1 = manager.findFragmentByTag(Demo3TwoFragment::class.java.simpleName)
            val f2 = manager.findFragmentByTag(Demo3ThreeFragment::class.java.simpleName)
            val f3 = manager.findFragmentByTag(Demo3FourFragment::class.java.simpleName)

            // 复用
            mDemo3OneFragment = if (f0 != null) {
                f0 as Demo3OneFragment
            } else {
                Demo3OneFragment.obtainFragment()
            }

            mDemo3TwoFragment = if (f1 != null) {
                f1 as Demo3TwoFragment
            } else {
                Demo3TwoFragment.obtainFragment()
            }

            mDemo3ThreeFragment = if (f2 != null) {
                f2 as Demo3ThreeFragment
            } else {
                Demo3ThreeFragment.obtainFragment()
            }

            mDemo3FourFragment = if (f3 != null) {
                f3 as Demo3FourFragment
            } else {
                Demo3FourFragment.obtainFragment()
            }
        }
    }

    // 退出的时候保存状态
    override fun onSaveInstanceState(outState: Bundle) {
        hideAllFragment(supportFragmentManager.beginTransaction())
        outState.putInt(EXTRA_SAVE_INDEX, mCurPosition)
        outState.putBoolean(EXTRA_IS_HOME_ACT_DESTROY, true)
        super.onSaveInstanceState(outState)
    }


    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun switchPage0() {
            switchFragment(0)
        }

        fun switchPage1() {
            switchFragment(1)
        }

        fun switchPage2() {
            switchFragment(2)
        }

        fun switchPage3() {
            switchFragment(3)
        }
    }

}