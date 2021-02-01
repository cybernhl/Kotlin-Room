package com.guadou.kt_demo.demo.demo6_imageselect_premision_rvgird

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.widget.ImageView
import com.guadou.kt_demo.R
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.databinding.ActivityDemo6Binding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.engine.extLoad
import com.guadou.lib_baselib.engine.extRequestPermission
import com.guadou.lib_baselib.engine.image_select.extOpenCamera
import com.guadou.lib_baselib.engine.image_select.extOpenImageSelect
import com.guadou.lib_baselib.ext.*
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener


/**
 * 相机相册
 */
class Demo6Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo6Binding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo6Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo6)
            .addBindingParams(BR.click, ClickProxy())
    }

    @SuppressLint("SetTextI18n")
    override fun startObserve() {

    }

    override fun init() {
        initRV()
    }

    private fun initRV() {

        mBinding.recyclerView
            .vertical(3)
            .bindData(mImageSelectDatas, R.layout.item_local_image) { holder, t, _ ->
                holder.getView<ImageView>(R.id.iv_img).extLoad(t.path)
            }
            .addItemDecoration(GridSpacingItemDecoration(3, dp2px(10f), true))
    }

    //已经选择的图片集合
    private var mImageSelectDatas = arrayListOf<LocalMedia>()


    private fun refreshRV() {
        toast("Refresh")
        mBinding.recyclerView.adapter?.notifyDataSetChanged()
    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        /**
         * 相机
         */
        fun selectAlbum() {
            //申请权限
            extRequestPermission(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                block = {

                    //获取到权限成功-打开单选相册
                    extOpenImageSelect(mImageSelectDatas, object : OnResultCallbackListener<LocalMedia> {

                        override fun onResult(result: MutableList<LocalMedia>?) {
                            result?.also {
                                mImageSelectDatas.clear()
                                mImageSelectDatas.addAll(it)

                                refreshRV()
                            }
                        }

                        override fun onCancel() {
                            toast("取消了选择")
                        }

                    }, selectNum = 1, canTackPhoto = true, canCompress = true, canCrop = true, ratioX = 1, ratioY = 1)

                }
            )
        }

        /**
         * 照相机
         */
        fun selectCamera() {
            //申请权限
            extRequestPermission(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                block = {

                    //获取到权限成功-打开单选相册
                    extOpenCamera(mImageSelectDatas, object : OnResultCallbackListener<LocalMedia> {

                        override fun onResult(result: MutableList<LocalMedia>?) {
                            result?.also {
                                mImageSelectDatas.clear()
                                mImageSelectDatas.addAll(it)

                                refreshRV()
                            }
                        }

                        override fun onCancel() {
                            toast("取消了选择")
                        }

                    }, canCompress = true, canCrop = true)

                }
            )
        }

        /**
         * 多选
         */
        fun muiltAlbum() {
            //申请权限
            extRequestPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                block = {

                    //获取到权限成功-打开单选相册
                    extOpenImageSelect(mImageSelectDatas, object : OnResultCallbackListener<LocalMedia> {

                        override fun onResult(result: MutableList<LocalMedia>?) {
                            result?.also {
                                mImageSelectDatas.clear()
                                mImageSelectDatas.addAll(it)

                                refreshRV()
                            }
                        }

                        override fun onCancel() {
                            toast("取消了选择")
                        }

                    }, selectNum = 9, canTackPhoto = false, canCompress = true, canCrop = false, ratioX = 1, ratioY = 1)

                }
            )
        }

    }

}