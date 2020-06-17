package com.guadou.kt_zoom.router

import com.guadou.cs_router.app.IAppComponentServer
import com.guadou.lib_baselib.ext.toast
import io.github.prototypez.appjoint.core.ServiceProvider

@ServiceProvider
class AppComponentImpl : IAppComponentServer {

    override fun sendMessag() {
        toast("通过路由发送消息")
    }

}