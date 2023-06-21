package com.guadou.kt_demo.demo.demo18_customview.takevideo1.utils

import android.util.Size
import java.lang.Long.signum

class CompareSizesByArea : Comparator<Size> {

    // We cast here to ensure the multiplications won't overflow
    override fun compare(lhs: Size, rhs: Size) =
        signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)
}