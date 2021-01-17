package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.popup

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.engine.extLoad
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.ImagePreviewUtils
import com.lxj.easyadapter.EasyAdapter
import com.lxj.easyadapter.ViewHolder
import kotlinx.android.synthetic.main.activity_demo_image_preview.*


class DemoImagePreviewActivity : BaseVMActivity<EmptyViewModel>() {

    var url1 =
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1549382334&di=332b0aa1ec4ccd293f176164d998e5ab&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D121ef3421a38534398c28f62fb7ada0b%2Ffaf2b2119313b07eedb4502606d7912397dd8c96.jpg"
    var url2 =
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548777981087&di=0618a101655e57c675c7c21b4ef55f00&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fitbbs%2F1504%2F06%2Fc70%2F5014635_1428321310010_mthumb.jpg"

    val list = listOf(
        "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2279952540,2544282724&fm=26&gp=0.jpg",
        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=851052518,4050485518&fm=26&gp=0.jpg",
        "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=174904559,2874238085&fm=26&gp=0.jpg",
        url1,
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1551692956639&di=8ee41e070c6a42addfc07522fda3b6c8&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20160413%2F75659e9b05b04eb8adf5b52669394897.jpg",
        "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2279952540,2544282724&fm=26&gp=0.jpg",
        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=851052518,4050485518&fm=26&gp=0.jpg",
        "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=174904559,2874238085&fm=26&gp=0.jpg",
        url2,
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1551692956639&di=8ee41e070c6a42addfc07522fda3b6c8&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20160413%2F75659e9b05b04eb8adf5b52669394897.jpg"
    )

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoImagePreviewActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_demo_image_preview

    override fun startObserve() {

    }

    override fun init() {

        //默认的单独图片预览
        image1.extLoad(url1, R.mipmap.ic_launcher, isForceOriginalSize = true, isCenterCrop = true)
        image1.click {
            ImagePreviewUtils.singleImagePreview(this, image1, url1, R.mipmap.ic_launcher)
        }

        //带圆角的单图片预览
        image2.extLoad(url2, R.mipmap.ic_launcher, roundRadius = 50, isForceOriginalSize = true, isCenterCrop = true)
        image2.click {
            ImagePreviewUtils.singleImagePreview(this, image2, url2, R.mipmap.ic_launcher, roundRadius = 50)
        }

        //在RV中的多图片预览
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = ImageAdapter(list)

        //在ViewPager中的展示
        viewPager.offscreenPageLimit = list.size
        viewPager.adapter = ImagePagerAdapter(list, viewPager)
    }

    //RV的数据适配器
    class ImageAdapter(private val list: List<String>) : EasyAdapter<String>(list, R.layout.adapter_image) {
        override fun bind(holder: ViewHolder, t: String, position: Int) {
            val imageView = holder.getView<ImageView>(R.id.image)
            imageView.extLoad(t, R.mipmap.ic_launcher, isForceOriginalSize = true)

            //设置点击事件 弹起多图的预览
            imageView.click {
                //开启多图预览
                ImagePreviewUtils.multipleImagePreview(
                    imageView.context,
                    imageView,
                    list,
                    position,
                    R.mipmap.ic_launcher
                ) { popupView, position ->
                    //当预览的图片滚动的时候需要选择是哪一个ImageView来生效了
                    val rv = holder.itemView.parent as RecyclerView
                    popupView.updateSrcView(rv.getChildAt(position) as ImageView)

                }
            }
        }

    }

    //ViewPager的数据适配器
    internal class ImagePagerAdapter(private val list: List<String>, private val pager: ViewPager) : PagerAdapter() {

        override fun getCount(): Int {
            return list.size
        }

        override fun isViewFromObject(view: View, o: Any): Boolean {
            return view === o
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = ImageView(container.context)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            container.addView(imageView)

            //1. 加载图片
            imageView.extLoad(list[position], R.mipmap.ic_launcher, isForceOriginalSize = true)

            //2. 设置点击
            imageView.click {
                //开启多图预览
                ImagePreviewUtils.multipleImagePreview(
                    imageView.context,
                    imageView,
                    list,
                    position,
                    R.mipmap.ic_launcher
                ) { popupView, position ->
                    //当预览的图片滚动的时候需要选择是哪一个ImageView来生效了
                    //1.pager更新当前显示的图片
                    //当启用isInfinite时，position会无限增大，需要映射为当前ViewPager中的页
                    val realPosi: Int = position % list.size

                    pager.setCurrentItem(realPosi, false)
                    //2.更新弹窗的srcView，注意这里的position是list中的position，上面ViewPager设置了pageLimit数量，
                    //保证能拿到child，如果不设置pageLimit，ViewPager默认最多维护3个page，会导致拿不到child
                    popupView.updateSrcView(pager.getChildAt(realPosi) as ImageView)

                }
            }
            return imageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}



