package com.guadou.kt_demo.demo.demo6_imageselect_premision_rvgird

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.widget.ImageView
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.EmptyViewModel
import com.guadou.lib_baselib.engine.extLoad
import com.guadou.lib_baselib.engine.extRequestPermission
import com.guadou.lib_baselib.engine.image_select.extOpenCamera
import com.guadou.lib_baselib.engine.image_select.extOpenImageSelect
import com.guadou.lib_baselib.ext.*
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.activity_demo6.*

/**
 * 相机相册
 */
class Demo6Activity : BaseActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo6Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }


    override fun inflateLayoutById(): Int = R.layout.activity_demo6

    @SuppressLint("SetTextI18n")
    override fun startObserve() {

    }

    override fun init() {

        initRV()
        initLitener()
    }

    private fun initRV() {

        recyclerView.vertical(3).bindData(mImageSelectDatas, R.layout.item_local_image) { holder, t, _ ->
            holder.getView<ImageView>(R.id.iv_img).extLoad(t.path)
        }.addItemDecoration(GridSpacingItemDecoration(3, dp2px(10f), true))
    }

    //已经选择的图片集合
    private var mImageSelectDatas = arrayListOf<LocalMedia>()

    private fun initLitener() {

        //单选相册
        btn_album_1.click {

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

        //打开相机
        btn_album_2.click {

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

        //多选相册
        btn_album_3.click {

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

    private fun refreshRV() {
        toast("Refresh")
        recyclerView.adapter?.notifyDataSetChanged()
    }

}