package com.guadou.kt_demo.demo.demo9_ktfollow


class Member(private val map: Map<String, Any>) {

    val name: String by map
    val age: Int by map
    val dob: Long by map

    override fun toString(): String {
        return "Member(name='$name', age=$age, dob=$dob)"
    }

}