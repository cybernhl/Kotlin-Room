package com.guadou.kt_demo.demo.demo9_ktfollow

import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class AllVariables {
    val userName: String by ProvideDelegates()
    val age: String by ProvideDelegates()
    val dob: String by ProvideDelegates()
}


class ProvideDelegates {
    operator fun provideDelegate(thisRef: AllVariables, property: KProperty<*>): ReadWriteProperty<AllVariables, String> {

        return when (property.name) {
            "userName" -> PropertyDelete()
            else -> throw Exception("property name not valid!")
        }
    }
}


class PropertyDelete : ReadWriteProperty<AllVariables, String> {
    override fun getValue(thisRef: AllVariables, property: KProperty<*>): String {
        return "赋值给他一个姓名"
    }

    override fun setValue(thisRef: AllVariables, property: KProperty<*>, value: String) {

    }
}
