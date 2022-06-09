package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.lai

import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.BaseInterceptImpl
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept.InterceptChain
import com.guadou.kt_demo.demo.demo5_network_request.Demo5Activity
import com.guadou.lib_baselib.view.FangIOSDialog

/**
 * 新用户的拦截
 */
class InterceptSkill(private val bean: JobInterceptBean) : BaseInterceptImpl() {

    override fun intercept(chain: InterceptChain) {
        super.intercept(chain)

        if (bean.isNeedSkill) {
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
            setTitle("你没有填写技能")
            setMessage("你要去填写技能吗？")
            setNegativeButton("跳过") {
                dismiss()
                chain.process()
            }
            setPositiveButton("去填写") {
                Demo5Activity.startInstance()
            }
            show()
        }
    }

}