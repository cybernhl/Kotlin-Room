package com.guadou.kt_demo.ui.demo4.banner

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.guadou.lib_baselib.ext.engine.extLoad
import com.youth.banner.adapter.BannerAdapter

/**
 * 默认的Bnaner的图片适配器
 */
class BannerImageAdapter(imageUrls: List<String>, val placeHolderRes: Int = 0) :

    BannerAdapter<String, BannerImageAdapter.ImageHolder>(imageUrls) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ImageHolder {
        val imageView = ImageView(parent!!.context)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        return ImageHolder(imageView)
    }

    override fun onBindView(holder: ImageHolder?, data: String?, position: Int, size: Int) {

        if (placeHolderRes == 0){
            holder?.imageView?.extLoad(data)
        }else{
            holder?.imageView?.extLoad(data, placeHolderRes)
        }
    }

    class ImageHolder(view: View) : RecyclerView.ViewHolder(view) {

        var imageView: ImageView = view as ImageView

    }

}

