package com.guadou.kt_demo.demo.demo8_recyclerview.base;

/**
 * 如果有多布局的控制接口，可以传入构造方法
 */
public interface IHasMoreType<T> {
    //根据当前item的数据，返回指定的布局id
    int getLayoutId(T t);
}
