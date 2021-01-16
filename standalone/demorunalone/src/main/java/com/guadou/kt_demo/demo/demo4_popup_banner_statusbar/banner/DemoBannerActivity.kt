package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.banner

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.indicator.RectangleIndicator
import com.youth.banner.transformer.AlphaPageTransformer
import kotlinx.android.synthetic.main.activity_demo_banner.*

/**
 * banner
 */
class DemoBannerActivity : BaseActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoBannerActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }


    override fun inflateLayoutById(): Int = R.layout.activity_demo_banner

    override fun startObserve() {

    }

    override fun init() {

        //第一个Banner
        val imageUrls = listOf(
            "http://yyjobs-admin-dev.guabean.com/storage/202008/04/eIFNU5y0ErhXy128fuBAUtoWDqgEhw8Ufh1xRu72.jpeg",
            "http:/yyjobs-admin-dev.guabean.com/storage/202008/04/fiXMOAhEQxzgLmLL0kMVDHb5NhqPY4LMSMANbqO5.jpeg",
            "http://yyjobs-admin-dev.guabean.com/storage/202007/02/pbfW3QK5b9ynHCWPPg2Son5UNj9D4i2hcqThmziX.png",
            "http://yyjobs-admin-dev.guabean.com/storage/202006/24/GvtI0hurETguAPedhR8saalhgRDCwg5Df84r8HEi.jpeg"
        )

        //默认的Banner
        banner1?.also {
            it.addBannerLifecycleObserver(this)
            it.indicator = CircleIndicator(this)
            it.adapter = BannerImageAdapter(imageUrls, R.mipmap.item_merchants_bg)

        }


        //矩形的指示器
        banner2?.also {
            it.addBannerLifecycleObserver(this)
            it.indicator = RectangleIndicator(this)
            it.adapter = BannerImageAdapter(imageUrls, R.mipmap.item_merchants_bg)

            it.addPageTransformer(AlphaPageTransformer())  //可以添加多种组合（带透明度变换）
            //事件
            it.setOnBannerListener { url, position ->
                toast("点击$position url:$url")
            }
        }


        //画廊效果
        banner3?.also {
            it.addBannerLifecycleObserver(this)
//            it.indicator = CircleIndicator(this)
            it.adapter = BannerImageAdapter(imageUrls, R.mipmap.item_merchants_bg)

            //默认画廊
            it.setBannerGalleryEffect(18, 10)
            //添加魅族效果
//            it.setBannerGalleryMZ(20)

            //可以和其他PageTransformer组合使用，比如AlphaPageTransformer，注意但和其他带有缩放的PageTransformer会显示冲突
            //addPageTransformer(new AlphaPageTransformer());
        }


        //自定义的指示器
        banner4?.also {
            it.addBannerLifecycleObserver(this)
            it.indicator = RectangleCircleIndicator(this)
            it.adapter = BannerImageAdapter(imageUrls, R.mipmap.item_merchants_bg)

            it.addPageTransformer(AlphaPageTransformer())  //可以添加多种组合（带透明度变换）
            //事件
            it.setOnBannerListener { url, position ->
                toast("点击$position url:$url")
            }
        }

        //如果是多布局-直接定义一个多布局的Adapter - 参考MultipleTypesAdapter.java


        //如果需要上下滚动 和垂直滚动的RV一样的一个Adapter
        //实现1号店和淘宝头条类似的效果 ，类型下面的做法
//        banner.setAdapter(TopLineAdapter(DataBean.getTestData2()))
//            .setOrientation(Banner.VERTICAL)
//            .setPageTransformer(ZoomOutPageTransformer())
//            .setOnBannerListener(OnBannerListener<*> { data: Any, position: Int ->
//                Snackbar.make(banner, (data as DataBean).title, Snackbar.LENGTH_SHORT).show()
//                LogUtils.d("position：$position")
//            })

    }

}