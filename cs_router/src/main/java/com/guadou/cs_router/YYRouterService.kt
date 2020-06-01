package com.guadou.cs_router


import com.guadou.cs_router.app.IAppComponentServer
import com.guadou.cs_router.main.IMainComponentServer

import io.github.prototypez.appjoint.AppJoint

object YYRouterService {

    var appComponentServer = AppJoint.service(IAppComponentServer::class.java)

    var mainComponentServer = AppJoint.service(IMainComponentServer::class.java)


}
