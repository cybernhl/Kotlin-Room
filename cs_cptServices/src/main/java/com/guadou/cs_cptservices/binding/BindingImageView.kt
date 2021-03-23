package com.guadou.cs_cptservices.binding

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.guadou.lib_baselib.engine.extLoad
import com.guadou.lib_baselib.utils.CommUtils

/**
 * 设置图片的加载
 */
@BindingAdapter("imgUrl", "placeholder", "roundRadius", "isCircle", requireAll = false)
fun loadImg(
    view: ImageView,
    url: Any?,
    placeholder: Drawable? = null,
    roundRadius: Int = 0,
    isCircle: Boolean = false
) {
    url?.let {
        view.extLoad(
            it,
            placeholder = placeholder,
            roundRadius = CommUtils.dip2px(roundRadius),
            isCircle = isCircle
        )
    }
}


@BindingAdapter("loadBitmap")
fun loadBitmap(view: ImageView, bitmap: Bitmap?) {
    view.setImageBitmap(bitmap)
}