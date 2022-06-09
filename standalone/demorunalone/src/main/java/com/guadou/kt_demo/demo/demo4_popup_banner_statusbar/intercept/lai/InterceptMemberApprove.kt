package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.lai

import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.BaseInterceptImpl
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.InterceptChain
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.view.FangIOSDialog

/**
 * 新用户的拦截
 */
class InterceptMemberApprove(private val bean: JobInterceptBean) : BaseInterceptImpl() {

    override fun intercept(chain: InterceptChain) {
        super.intercept(chain)

        if (!bean.isMemberApprove) {
            //拦截
            showDialogTips(chain)
        } else {
            //放行- 转交给下一个拦截器
            chain.process()
        }
    }

    private fun showDialogTips(chain: InterceptChain) {
        FangIOSDialog(chain.activity).apply {
            setTitle("状态不对")
            setMessage("你用户状态不对，联系管理员吗？")
            setNegativeButton("跳过") {
                dismiss()
                chain.process()
            }
            setPositiveButton("联系") {
                dismiss()
                toast("去拨打电话，当你状态对了才能继续")
            }
            show()
        }
    }

}