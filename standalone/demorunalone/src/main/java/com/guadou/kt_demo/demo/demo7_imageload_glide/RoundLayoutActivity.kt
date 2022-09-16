package com.guadou.kt_demo.demo.demo7_imageload_glide


import android.graphics.*
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo7_imageload_glide.layout.RoundCircleLinearLayout
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.CommUtils


class RoundLayoutActivity : BaseVMActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().gotoActivity<RoundLayoutActivity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_round_layout


    override fun startObserve() {

    }

    override fun init() {

        val layout_3 = findViewById<RoundCircleLinearLayout>(R.id.layout_3)

        findViewById<ViewGroup>(R.id.layout_2).click {
            toast("12345")

            it.background = drawable(R.drawable.chengxiao)
            layout_3.background = drawable(R.drawable.chengxiao)
        }



//        val iv_content = findViewById<ImageView>(R.id.iv_content)
//
//        val source: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.chengxiao)
//
//        val mask: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.triangle)
//
//
//        //穿件一个空的Bitmap用来存放合成的图片
//        val result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
//
//        val canvas = Canvas(result)
//        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//        paint.color = Color.BLACK
//
//        canvas.drawBitmap(
//            mask,
//            ((source.width / 2) - (mask.width / 2)).toFloat(),
//            ((source.height / 2) - (mask.height / 2)).toFloat(),
//            paint
//        )
//
//        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
//
//        canvas.drawBitmap(source, 0f, 0f, paint)
//
//        paint.setXfermode(null)
//
//        iv_content.setImageBitmap(result)


//        iv_content.outlineProvider = object : ViewOutlineProvider() {
//
//            override fun getOutline(view: View, outline: Outline) {
//
//                val x: Int = (source.width / 2) - (mask.width / 2)
//                val y: Int = (source.height / 2) - (mask.height / 2)
//
//                val path = Path()
//                path.moveTo(x.toFloat(), y.toFloat())
//                path.lineTo((x + mask.width).toFloat(), y.toFloat())
//                path.lineTo((x + mask.width / 2).toFloat(), (y + mask.height).toFloat())
//                path.lineTo(x.toFloat(), y.toFloat())
//                path.close()
//
//                outline.setConvexPath(path)
//            }
//        }
    }
}