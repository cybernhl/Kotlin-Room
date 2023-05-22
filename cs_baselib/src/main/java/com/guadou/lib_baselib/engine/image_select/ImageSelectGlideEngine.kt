package com.guadou.lib_baselib.engine.image_select


import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.guadou.basiclib.R
import com.guadou.lib_baselib.glideconfig.GlideApp
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.utils.ActivityCompatHelper



/**
 * 图片选择-Glide加载引擎
 */
internal class ImageSelectGlideEngine private constructor() : ImageEngine {

    companion object {
        private var instance: ImageSelectGlideEngine? = null
            get() {
                if (field == null) {
                    field = ImageSelectGlideEngine()
                }
                return field
            }

        fun get(): ImageSelectGlideEngine {
            return instance!!
        }
    }


    /**
     * 加载图片
     *
     * @param context   上下文
     * @param url       资源url
     * @param imageView 图片承载控件
     */
    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }

        GlideApp.with(context)
            .load(url)
            .into(imageView)
    }

    override fun loadImage(context: Context, imageView: ImageView, url: String, maxWidth: Int, maxHeight: Int) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }

        GlideApp.with(context)
            .load(url)
            .override(maxWidth, maxHeight)
            .into(imageView)
    }

    /**
     * 加载相册目录封面
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadAlbumCover(context: Context, url: String, imageView: ImageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }

        GlideApp.with(context)
            .asBitmap()
            .load(url)
            .override(180, 180)
            .sizeMultiplier(0.5f)
            .transform(CenterCrop(), RoundedCorners(8))
            .placeholder(R.drawable.ps_image_placeholder)
            .into(imageView)
    }


    /**
     * 加载图片列表图片
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }

        GlideApp.with(context)
            .load(url)
            .override(200, 200)
            .centerCrop()
            .placeholder(R.drawable.ps_image_placeholder)
            .into(imageView)
    }

    override fun pauseRequests(context: Context) {

        GlideApp.with(context).pauseRequests()
    }

    override fun resumeRequests(context: Context) {

        GlideApp.with(context).resumeRequests()
    }


}