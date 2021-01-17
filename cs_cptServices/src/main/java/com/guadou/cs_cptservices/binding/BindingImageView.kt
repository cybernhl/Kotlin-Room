package com.guadou.cs_cptservices.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.guadou.lib_baselib.engine.extLoad
import com.guadou.lib_baselib.utils.CommUtils

/**
 * 设置图片的加载
 */
@BindingAdapter("imgUrl", "placeholder", "placeholderRes", "roundRadius", "isCircle", requireAll = false)
fun loadImg(
    view: ImageView,
    url: Any?,
    placeholder: Drawable? = null,
    placeholderRes: Int = 0,
    roundRadius: Int = 0,
    isCircle: Boolean = false
) {
    url?.let {
        view.extLoad(
            it,
            placeholder = placeholder,
            placeholderRes = placeholderRes,
            roundRadius = CommUtils.dip2px(roundRadius),
            isCircle = isCircle
        )
    }
}