package com.guadou.kt_demo.demo.demo7_imageload_glide.layout

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo7_imageload_glide.layout.view.IRoundCircleView
import com.guadou.kt_demo.demo.demo7_imageload_glide.layout.view.RoundCircleViewImpl


class RoundCircleNestedScrollView : NestedScrollView, IRoundCircleView {

    private lateinit var roundCircleViewImpl: RoundCircleViewImpl

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(this, context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(this, context, attrs)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        roundCircleViewImpl.onLayout(changed, left, top, right, bottom)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        roundCircleViewImpl.beforeDispatchDraw(canvas)
        super.dispatchDraw(canvas)
        roundCircleViewImpl.afterDispatchDraw(canvas)
    }

    override fun onDraw(canvas: Canvas?) {
        if (roundCircleViewImpl.onDraw(canvas)) {
            super.onDraw(canvas)
        }
    }

    private fun init(view: View, context: Context, attributeSet: AttributeSet?) {
        roundCircleViewImpl = RoundCircleViewImpl(
            view,
            context,
            attributeSet,
            R.styleable.RoundCircleNestedScrollView,
            intArrayOf(
                R.styleable.RoundCircleNestedScrollView_is_circle,
                R.styleable.RoundCircleNestedScrollView_round_radius,
                R.styleable.RoundCircleNestedScrollView_topLeft,
                R.styleable.RoundCircleNestedScrollView_topRight,
                R.styleable.RoundCircleNestedScrollView_bottomLeft,
                R.styleable.RoundCircleNestedScrollView_bottomRight,
                R.styleable.RoundCircleNestedScrollView_round_circle_background_color,
                R.styleable.RoundCircleNestedScrollView_round_circle_background_drawable,
                R.styleable.RoundCircleNestedScrollView_is_bg_center_crop,
            )

        )
        nativeBgDrawable?.let {
            roundCircleViewImpl.setNativeDrawable(it)
        }
    }

    private var nativeBgDrawable: Drawable? = null
    override fun setBackground(background: Drawable?) {
        if (!this::roundCircleViewImpl.isInitialized) {
            nativeBgDrawable = background
        } else {
            roundCircleViewImpl.setBackground(background)
        }
    }

    override fun setBackgroundColor(color: Int) {
        if (!this::roundCircleViewImpl.isInitialized) {
            nativeBgDrawable = ColorDrawable(color)
        } else {
            roundCircleViewImpl.setBackground(background)
        }
    }

    override fun setBackgroundResource(resid: Int) {
        if (!this::roundCircleViewImpl.isInitialized) {
            nativeBgDrawable = context.resources.getDrawable(resid)
        } else {
            roundCircleViewImpl.setBackground(background)
        }
    }

    override fun setBackgroundDrawable(background: Drawable?) {
        if (!this::roundCircleViewImpl.isInitialized) {
            nativeBgDrawable = background
        } else {
            roundCircleViewImpl.setBackground(background)
        }
    }
}