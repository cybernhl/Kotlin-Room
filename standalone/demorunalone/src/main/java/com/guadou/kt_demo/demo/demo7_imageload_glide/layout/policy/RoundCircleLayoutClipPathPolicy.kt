//package com.guadou.kt_demo.demo.demo7_imageload_glide.layout.policy
//
//import android.content.Context
//import android.graphics.*
//import android.util.AttributeSet
//import android.view.View
//import com.guadou.lib_baselib.utils.log.YYLogUtils
//
//
//internal class RoundCircleLayoutClipPathPolicy(
//    view: View, context: Context, attributeSet: AttributeSet?,
//    attrs: IntArray,
//    attrIndex: IntArray
//) : AbsRoundCirclePolicy(view, context, attributeSet, attrs, attrIndex) {
//
//    init {
//        initViewData()
//    }
//
//    private lateinit var mPath: Path
//    private lateinit var mRectF: RectF
//
//    private fun initViewData() {
//        mRectF = RectF()
//        mPath = Path()
//    }
//
//    override fun beforeDispatchDraw(canvas: Canvas?) {
//        canvas?.clipPath(mPath)
//    }
//
//    override fun afterDispatchDraw(canvas: Canvas?) {
//
//    }
//
//    override fun onLayout(left: Int, top: Int, right: Int, bottom: Int) {
//        mRectF.set(0f, 0f, mContainer.width.toFloat(), mContainer.height.toFloat())
//        resetRoundPath()
//    }
//
//    private fun resetRoundPath() {
//        mPath.reset()
//
//        if (isCircleType) {
//
//            mPath.addOval(0f, 0f, mContainer.width.toFloat(), mContainer.height.toFloat(), Path.Direction.CCW)
//
//        } else {
//
//            //如果是圆角-裁剪圆角
//            if (mTopLeft > 0 || mTopRight > 0 || mBottomLeft > 0 || mBottomRight > 0) {
//
//                mPath.addRoundRect(
//                    calculateBounds(),
//                    floatArrayOf(mTopLeft, mTopLeft, mTopRight, mTopRight, mBottomRight, mBottomRight, mBottomLeft, mBottomLeft),
//                    Path.Direction.CCW
//                )
//
//            } else {
//
//                mPath.addRoundRect(mRectF, mRoundRadius, mRoundRadius, Path.Direction.CCW)
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