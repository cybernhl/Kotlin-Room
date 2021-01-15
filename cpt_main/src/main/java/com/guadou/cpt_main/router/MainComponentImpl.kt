package com.guadou.cpt_main.router

import com.guadou.cpt_main.ui.AuthActivity
import com.guadou.cs_router.main.IMainComponentServer
import io.github.prototypez.appjoint.core.ServiceProvider

@ServiceProvider
class MainComponentImpl : IMainComponentServer {

    override fun startMainActivity() {

    }

    override fun startAuthActivity() {
        AuthActivity.startInstance()
    }

}