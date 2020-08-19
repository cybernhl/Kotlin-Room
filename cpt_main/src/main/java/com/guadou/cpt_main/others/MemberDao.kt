package com.guadou.cpt_main.others

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberDao @Inject constructor() {

    fun printUser(): String {
        return "User Name"
    }
}