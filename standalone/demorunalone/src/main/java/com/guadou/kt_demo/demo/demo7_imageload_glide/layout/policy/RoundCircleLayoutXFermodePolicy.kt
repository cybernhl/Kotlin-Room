//package com.guadou.kt_demo.demo.demo7_imageload_glide.layout.policy
//
//import android.content.Context
//import android.graphics.*
//import android.os.Build
//import android.util.AttributeSet
//import android.view.View
//
//
//internal class RoundCircleLayoutXFermodePolicy(
//    view: View, context: Context, attributeSet: AttributeSet?,
//    attrs: IntArray,
//    attrIndex: IntArray
//) : AbsRoundCirclePolicy(view, context, attributeSet, attrs, attrIndex) {
//
//    init {
//        initViewData()
//    }
//
//    private lateinit var mPaint: Paint
//    private lateinit var mRectF: RectF
//    private lateinit var mPath: Path
////    private var sc: Int? = null
//    private var mXfermode: PorterDuffXfermode? = null
//
//    override fun beforeDispatchDraw(canvas: Canvas?) {
////        canvas?.save()
//        canvas?.saveLayer(0f, 0f,mContainer.width.toFloat(),mContainer.height.toFloat(), null, Canvas.ALL_SAVE_FLAG);
////        sc = canvas?.saveLayer(0f, 0f, mContainer.width.toFloat(), mContainer.height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
//        mPaint.xfermode = mXfermode
//    }
//
//    override fun afterDispatchDraw(canvas: Canvas?) {
//
//        canvas?.drawPath(mPath, mPaint)
//
//        canvas?.restore()
//
//        mPaint.xfermode = null
////        sc?.let {
////            canvas?.restoreToCount(it)
////        }
//
//    }
//
//    override fun onLayout(left: Int, top: Int, right: Int, bottom: Int) {
//        mRectF.set(0f, 0f, mContainer.width.toFloat(), mContainer.height.toFloat())
//        resetRoundPath()
//    }
//
//    private fun initViewData() {
//        //在api11到api18之间设置禁用硬件加速
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
//            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
//        ) {
//            mContainer.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//        }
//
//        mRectF = RectF()
//        mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
//        mPaint.setColor(Color.TRANSPARENT)
//        mPaint.isAntiAlias = true
//        mXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
//        mPath = Path()
//
//    }
//
//    private fun resetRoundPath() {
//        mPath.reset()
//
//        if (isCircleType) {
//
//            mPath.addOval(0f, 0f, mContainer.width.toFloat(), mContainer.height.toFloat(), Path.Direction.CW)
//
//        } else {
//
//            //如果是圆角-裁剪圆角
//            if (mTopLeft > 0 || mTopRight > 0 || mBottomLeft > 0 || mBottomRight > 0) {
//
//                mPath.addRoundRect(
//                    calculateBounds(),
//                    floatArrayOf(mTopLeft, mTopLeft, mTopRight, mTopRight, mBottomRight, mBottomRight, mBottomLeft, mBottomLeft),
//                    Path.Direction.CW
//                )
//
//            } else {
//
//                mPath.addRoundRect(mRectF, mRoundRadius, mRoundRadius, Path.Direction.CW)
//            }
//
//        }
//
//    }
//
//    override fun setCornerRadius(cornerRadius: Float) {
//        this.mRoundRadius = cornerRadius
//        resetRoundPath()
//    }
//}