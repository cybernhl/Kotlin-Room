package com.guadou.cs_cptservices.binding

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.guadou.lib_baselib.font_text_view.TypefaceUtil

/**
 * 文本的设置
 */

/**
 * 下划线
 */
@BindingAdapter("isUnderline")
fun setUnderline(textView: TextView, isUnderline: Boolean) {
    if (isUnderline) {
        textView.paint.flags = Paint.UNDERLINE_TEXT_FLAG//下划线
        textView.paint.isAntiAlias = true //抗锯齿
    } else {
        textView.paint.flags = 0
    }
}

@BindingAdapter("isCenterLine")
fun isCenterLine(textView: TextView, isUnderline: Boolean) {
    if (isUnderline) {
        textView.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        textView.paint.isAntiAlias = true //抗锯齿
    } else {
        textView.paint.flags = 0
    }
}

/**
 * 设置字体
 */
@BindingAdapter("typefaceLight")
fun TypefaceLight(textView: TextView, boolean: Boolean) {
    textView.typeface = TypefaceUtil.getSFLight(textView.context)
}

@BindingAdapter("typefaceRegular")
fun TypefaceRegular(textView: TextView, boolean: Boolean) {
    textView.typeface = TypefaceUtil.getSFRegular(textView.context)
}

@BindingAdapter("typefaceMedium")
fun TypefaceMedium(textView: TextView, boolean: Boolean) {
    textView.typeface = TypefaceUtil.getSFMedium(textView.context)
}

@BindingAdapter("typefaceFlower")
fun TypefaceFlower(textView: TextView, boolean: Boolean) {
    textView.typeface = TypefaceUtil.getSFFlower(textView.context)
}

@BindingAdapter("typefaceSemiBold")
fun TypefaceSemiBold(textView: TextView, boolean: Boolean) {
    textView.typeface = TypefaceUtil.getSFSemobold(textView.context)
}

@BindingAdapter("setRightDrawable")
fun setRightDrawable(textView: TextView, drawable: Drawable?) {
    if (drawable == null) {
        textView.setCompoundDrawables(null, null, null, null)
    } else {
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        textView.setCompoundDrawables(null, null, drawable, null)
    }
}

@BindingAdapter("setLeftDrawable")
fun setLeftDrawable(textView: TextView, drawable: Drawable?) {
    if (drawable == null) {
        textView.setCompoundDrawables(null, null, null, null)
    } else {
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        textView.setCompoundDrawables(drawable, null, null, null)
    }
}

@BindingAdapter("text", "default", requireAll = false)
fun setText(view: TextView, text: CharSequence?, default: String?) {
    if (text == null || text.trim() == "" || text.contains("null")) {
        view.text = default
    } else {
        view.text = text
    }
}