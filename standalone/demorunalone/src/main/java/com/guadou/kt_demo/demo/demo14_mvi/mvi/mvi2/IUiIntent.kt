package com.guadou.kt_demo.demo.demo14_mvi.mvi.mvi2

import androidx.annotation.Keep

//MVI 页面事件管理的基类
//在R8代码混淆压缩时必须保留被标记的类或方法，以防止代码出现因混淆而导致的崩溃。
@Keep
interface IUiIntent