package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//不需要回调的处理--由于兼容性问题太多，单独想看AOP的可以切换git-aop分支
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
}

