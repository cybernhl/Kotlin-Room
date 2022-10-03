package com.guadou.kt_demo.demo.demo16_record.viewpager

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.guadou.lib_baselib.base.fragment.BaseVMFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.ext.bindFragment
import com.guadou.lib_baselib.utils.log.YYLogUtils

internal class VPItemFragment(private var bgColor: Int) : BaseVMFragment<EmptyViewModel>() {

    override fun getLayoutIdRes(): Int {
        return R.layout.fragment_vp_item
    }

    override fun startObserve() {}

    override fun init() {

    }

    override fun initViews(view: View) {
        val llRoot = view.findViewById<View>(R.id.ll_root)
        llRoot.setBackgroundColor(bgColor)

        view.findViewById<ViewPager>(R.id.viewPager).apply {

            val fragmentList = listOf(VPItemChildFragment(0), VPItemChildFragment(1), VPItemChildFragment(2))
            bindFragment(
                childFragmentManager,
//                lifecycle,
                fragmentList,
                behavior = 1
            )

//            orientation = ViewPager2.ORIENTATION_HORIZONTAL

//            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//                    //这个view是的fragment。
//                    val view = fragmentList[position].view
//                    view?.let {
//                        updatePagerHeightForChild(view, this@apply)
//                    }
//                }
//            })

        }

    }


    //解决viewpager2高度问题
    private fun updatePagerHeightForChild(view: View, pager: ViewPager2) {

        view.post {
            val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)

            YYLogUtils.w("解决viewpager2高度问题，两个高度： ${pager.layoutParams.height} ${view.measuredHeight}")
            if (pager.layoutParams.height != view.measuredHeight) {
                pager.layoutParams = (pager.layoutParams)
                    .also { lp ->
                        lp.height = view.measuredHeight
                    }
            }
        }
    }

}