package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.lai

import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.BaseInterceptImpl
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.InterceptChain
import com.guadou.kt_demo.demo.demo5_network_request.Demo5Activity

/**
 * 新用户的拦截
 */
class InterceptNewMember(private val bean: JobInterceptBean) : BaseInterceptImpl() {

    override fun intercept(chain: InterceptChain) {
        super.intercept(chain)

        if (bean.isNewMember) {
            //拦截
            //可以不弹窗，直接就暴力跳转新页面
            Demo5Activity.startInstance()
        } else {
            //放行- 转交给下一个拦截器
            chain.process()
        }
    }


    //已经完成了培训-放行
    fun resetNewMember() {
        mChain?.process()
    }

}