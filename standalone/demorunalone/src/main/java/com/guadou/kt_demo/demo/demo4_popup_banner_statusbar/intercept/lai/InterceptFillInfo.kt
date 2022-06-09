package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.lai

import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.BaseInterceptImpl
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.InterceptChain
import com.guadou.kt_demo.demo.demo5_network_request.Demo5Activity
import com.guadou.lib_baselib.view.FangIOSDialog

/**
 * 完善个人信息
 */
class InterceptFillInfo(private val bean: JobInterceptBean) : BaseInterceptImpl() {

    override fun intercept(chain: InterceptChain) {
        super.intercept(chain)

        if (!bean.isFillInfo) {
            //拦截
            //跳转新页面
            showDialogTips(chain)
        } else {
            //放行- 转交给下一个拦截器
            chain.process()
        }
    }

    private fun showDialogTips(chain: InterceptChain) {
        FangIOSDialog(chain.activity).apply {
            setTitle("完善信息")
            setMessage("你没有完善信息，你要去完善信息")
            setNegativeButton("跳过"){
                dismiss()
                chain.process()
            }
            setPositiveButton("去完善"){
                Demo5Activity.startInstance()
            }
            show()
        }
    }

}