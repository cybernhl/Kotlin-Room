package com.guadou.lib_baselib.engine.image_select

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.guadou.basiclib.R
import com.guadou.lib_baselib.glideconfig.GlideApp
import com.guadou.lib_baselib.utils.CommUtils.getContext
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.luck.picture.lib.animators.AnimationType
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.*
import com.luck.picture.lib.config.SelectMimeType.ofImage
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.engine.CropFileEngine
import com.luck.picture.lib.engine.UriToFileTransformEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.interfaces.OnSelectLimitTipsListener
import com.luck.picture.lib.manager.PictureCacheManager
import com.luck.picture.lib.style.BottomNavBarStyle
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.style.SelectMainStyle
import com.luck.picture.lib.style.TitleBarStyle
import com.luck.picture.lib.utils.*
import com.luck.picture.lib.utils.ActivityCompatHelper.assertValidRequest
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropImageEngine
import com.yalantis.ucrop.model.AspectRatio
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File


/**
 * 图片选择的引擎类
 * 用于开启图片选择相关，压缩相关，裁剪相关
 *
 * openImageSelect -  开启图库的选择（选择数量，是否裁剪，是否压缩）
 * openCamera  -  开启相机的选择 (是否裁剪，是否压缩)
 *
 */
//开启图片选择
fun Activity.extOpenImageSelect(
    selected: List<LocalMedia>?,
    listener: OnResultCallbackListener<LocalMedia>,
    selectNum: Int = 1,
    canTackPhoto: Boolean = true,
    canCrop: Boolean = false,
    canCompress: Boolean = true,
    ratioX: Int = 1,  //裁剪比例
    ratioY: Int = 1,
    clickSound: Boolean = false
) {

    //创建自定义的主题
    val selectorStyle = PictureSelectorStyle()

    // 主体风格
    val numberSelectMainStyle = SelectMainStyle()
    numberSelectMainStyle.isSelectNumberStyle = true
    numberSelectMainStyle.isPreviewSelectNumberStyle = false
    numberSelectMainStyle.isPreviewDisplaySelectGallery = true
    numberSelectMainStyle.selectBackground = R.drawable.ps_default_num_selector
    numberSelectMainStyle.previewSelectBackground = R.drawable.ps_preview_checkbox_selector
    numberSelectMainStyle.selectNormalBackgroundResources = R.drawable.ps_select_complete_normal_bg
    numberSelectMainStyle.selectNormalTextColor = ContextCompat.getColor(getContext(), R.color.ps_color_53575e)
    numberSelectMainStyle.selectNormalText = getString(R.string.ps_send)
    numberSelectMainStyle.adapterPreviewGalleryBackgroundResource = R.drawable.ps_preview_gallery_bg
    numberSelectMainStyle.adapterPreviewGalleryItemSize = DensityUtil.dip2px(getContext(), 52f)
    numberSelectMainStyle.previewSelectText = getString(R.string.ps_select)
    numberSelectMainStyle.previewSelectTextSize = 14
    numberSelectMainStyle.previewSelectTextColor = ContextCompat.getColor(getContext(), R.color.ps_color_white)
    numberSelectMainStyle.previewSelectMarginRight = DensityUtil.dip2px(getContext(), 6f)
    numberSelectMainStyle.selectBackgroundResources = R.drawable.ps_select_complete_bg
    numberSelectMainStyle.selectText = getString(R.string.ps_send_num)
    numberSelectMainStyle.selectTextColor = ContextCompat.getColor(getContext(), R.color.ps_color_white)
    numberSelectMainStyle.mainListBackgroundColor = ContextCompat.getColor(getContext(), R.color.ps_color_black)
    numberSelectMainStyle.isCompleteSelectRelativeTop = true
    numberSelectMainStyle.isPreviewSelectRelativeBottom = true
    numberSelectMainStyle.isAdapterItemIncludeEdge = false

    // 头部TitleBar 风格
    val numberTitleBarStyle = TitleBarStyle()
    numberTitleBarStyle.isHideCancelButton = true
    numberTitleBarStyle.isAlbumTitleRelativeLeft = true
    numberTitleBarStyle.titleAlbumBackgroundResource = R.drawable.ps_album_bg
    numberTitleBarStyle.titleDrawableRightResource = R.drawable.ps_ic_grey_arrow
    numberTitleBarStyle.previewTitleLeftBackResource = R.drawable.ps_ic_normal_back

    // 底部NavBar 风格
    val numberBottomNavBarStyle = BottomNavBarStyle()
    numberBottomNavBarStyle.bottomPreviewNarBarBackgroundColor = ContextCompat.getColor(getContext(), R.color.ps_color_half_grey)
    numberBottomNavBarStyle.bottomPreviewNormalText = getString(R.string.ps_preview)
    numberBottomNavBarStyle.bottomPreviewNormalTextColor = ContextCompat.getColor(getContext(), R.color.ps_color_9b)
    numberBottomNavBarStyle.bottomPreviewNormalTextSize = 16
    numberBottomNavBarStyle.isCompleteCountTips = false
    numberBottomNavBarStyle.bottomPreviewSelectText = getString(R.string.ps_preview_num)
    numberBottomNavBarStyle.bottomPreviewSelectTextColor = ContextCompat.getColor(getContext(), R.color.ps_color_white)

    //自定义主题设置对应的主题样式，标题样式，底部导航样式
    selectorStyle.selectMainStyle = numberSelectMainStyle
    selectorStyle.titleBarStyle = numberTitleBarStyle
    selectorStyle.bottomBarStyle = numberBottomNavBarStyle


    // 正式开启相册
    PictureSelector.create(this)
        .openGallery(ofImage())
        .setSelectorUIStyle(selectorStyle)   //设置自定义样式
        .setImageEngine(ImageSelectGlideEngine.get())
        .isMaxSelectEnabledMask(true)  //到达最大数量之后是否展示蒙层
        .isDirectReturnSingle(false)
        .setMaxSelectNum(selectNum)            //最大选择数量
        .setMaxVideoSelectNum(1)
        .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)  //选中动画
        .setSelectedData(selected)   //已经选中的资源
        .isWithSelectVideoImage(false)
        .isPreviewFullScreenMode(true)
        .isVideoPauseResumePlay(false)
        .isPreviewZoomEffect(true)
        .isPreviewImage(true)   //是否可预览图片视频音频
        .isPreviewVideo(true)
        .isPreviewAudio(true)
        .isDisplayTimeAxis(true)  //显示资源时间轴
        .isOnlyObtainSandboxDir(false)  //查询指定目录
        .isPageStrategy(false)    //分页模式
        .isOriginalControl(false)   //是否开启原图功能
        .isDisplayCamera(canTackPhoto)  // 显示or隐藏拍摄
        .isOpenClickSound(clickSound)   //点击的声音
        .setSkipCropMimeType(PictureMimeType.ofGIF(), PictureMimeType.ofWEBP())   //WebP与Gif跳过压缩
        .isFastSlidingSelect(true)  // 滑动选择
        .setSelectionMode(if (selectNum > 1) SelectModeConfig.MULTIPLE else SelectModeConfig.SINGLE)
//        .setLanguage(language)   //设置语言
        .setQueryFilterListener { false }
//        .setCustomLoadingListener(getCustomLoadingListener())   //自定义Loading
        .isAutoVideoPlay(true)  //预览视频自动播放
        .isLoopAutoVideoPlay(true)  //预览视频自动循环播放
        .isPageSyncAlbumCount(true)
        .isOriginalControl(false)  //是否开启原图功能
        //配置各种引擎
        .setSandboxFileEngine(MeSandboxFileEngine())
        .setCompressEngine(if (canCompress) ImageFileCompressEngine() else null)
        .setCropEngine(if (canCrop) ImageFileCropEngine(selectorStyle, ratioX, ratioY) else null)
//        .setSelectLimitTipsListener(MeOnSelectLimitTipsListener())  //自定义拦截提示
//        .setAddBitmapWatermarkListener(getAddBitmapWatermarkListener())  //给图片添加水印
//        .setVideoThumbnailListener(getVideoThumbnailEventListener())  //处理视频的缩略图
//        .setPermissionDescriptionListener(getPermissionDescriptionListener())  //权限说明提示
        .forResult(listener)  //结果的监听回调

}


//自定义沙盒文件处理(把外部目录下的图片拷贝至沙盒内IO流)
private class MeSandboxFileEngine : UriToFileTransformEngine {
    override fun onUriToFileAsyncTransform(context: Context, srcPath: String, mineType: String, call: OnKeyValueResultCallbackListener?) {
        call?.onCallback(srcPath, SandboxTransformUtils.copyPathToSandbox(context, srcPath, mineType))
    }
}

// 拦截自定义提示
private class MeOnSelectLimitTipsListener : OnSelectLimitTipsListener {
    override fun onSelectLimitTips(context: Context, media: LocalMedia?, config: SelectorConfig, limitType: Int): Boolean {
        if (limitType == SelectLimitType.SELECT_MIN_SELECT_LIMIT) {
            ToastUtils.showToast(context, "图片最少不能低于" + config.minSelectNum + "张")
            return true
        } else if (limitType == SelectLimitType.SELECT_MIN_VIDEO_SELECT_LIMIT) {
            ToastUtils.showToast(context, "视频最少不能低于" + config.minVideoSelectNum + "个")
            return true
        } else if (limitType == SelectLimitType.SELECT_MIN_AUDIO_SELECT_LIMIT) {
            ToastUtils.showToast(context, "音频最少不能低于" + config.minAudioSelectNum + "个")
            return true
        }
        return false
    }
}

//自定义压缩引擎
private class ImageFileCompressEngine : CompressFileEngine {
    override fun onStartCompress(context: Context, source: ArrayList<Uri>, call: OnKeyValueResultCallbackListener?) {
        //使用自带的Luban压缩库
        Luban.with(context).load(source).ignoreBy(100).setRenameListener { filePath ->
            val indexOf = filePath.lastIndexOf(".")
            val postfix = if (indexOf != -1) filePath.substring(indexOf) else ".jpg"
            DateUtils.getCreateFileName("CMP_") + postfix
        }.filter { path ->
            //过滤不能压缩的文件类型
            if (PictureMimeType.isUrlHasImage(path) && !PictureMimeType.isHasHttp(path)) {
                true
            } else {
                !PictureMimeType.isUrlHasGif(path)
            }
        }.setCompressListener(object : OnNewCompressListener {
            //压缩的监听
            override fun onStart() {}
            override fun onSuccess(source: String, compressFile: File) {
                call?.onCallback(source, compressFile.absolutePath)
            }

            override fun onError(source: String, e: Throwable) {
                call?.onCallback(source, null)

            }
        }).launch()
    }
}

//自定义图片裁剪引擎
private class ImageFileCropEngine(private val selectorStyle: PictureSelectorStyle?, private val cropX: Int, private val cropY: Int) : CropFileEngine {

    override fun onStartCrop(fragment: Fragment, srcUri: Uri, destinationUri: Uri, dataSource: ArrayList<String>, requestCode: Int) {

        //UCrop的配置文件
        val options = UCrop.Options()
        options.withAspectRatio(cropX.toFloat(), cropY.toFloat())  //裁剪比例
        options.setMultipleCropAspectRatio(                     //多图的裁剪比例
            AspectRatio("", cropX.toFloat(), cropY.toFloat()),
            AspectRatio("", cropX.toFloat(), cropY.toFloat())
        )
        options.setHideBottomControls(true)  //是否隐藏底部的裁剪菜单栏
        options.setFreeStyleCropEnabled(false)  //裁剪框or图片拖动
        options.setShowCropFrame(true)  //是否显示裁剪边框
        options.setShowCropGrid(true)  //是否显示裁剪框网格
        options.setCircleDimmedLayer(false)   //圆形头像裁剪模式
        options.setCropOutputPathDir(getSandboxPath())   //输出的位置-放在自定义的沙盒文件中
        options.isCropDragSmoothToCenter(false)  //图片是否跟随裁剪框居中
        options.setSkipCropMimeType(PictureMimeType.ofGIF(), PictureMimeType.ofWEBP())
        options.isForbidCropGifWebp(true)   //是否跳过GIF。WebP裁剪
        options.isForbidSkipMultipleCrop(true)  //多个裁剪时是否禁止跳过 ，true就是不能跳过，必须得一张一张裁剪
        options.setMaxScaleMultiplier(100f)

        //UCrop的样式配置
        if (selectorStyle != null && selectorStyle.selectMainStyle.statusBarColor != 0) {
            val mainStyle: SelectMainStyle = selectorStyle.selectMainStyle
            val isDarkStatusBarBlack = mainStyle.isDarkStatusBarBlack
            val statusBarColor = mainStyle.statusBarColor
            options.isDarkStatusBarBlack(isDarkStatusBarBlack)
            if (StyleUtils.checkStyleValidity(statusBarColor)) {
                options.setStatusBarColor(statusBarColor)
                options.setToolbarColor(statusBarColor)
            } else {
                options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.ps_color_grey))
                options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.ps_color_grey))
            }
            val titleBarStyle: TitleBarStyle = selectorStyle.titleBarStyle
            if (StyleUtils.checkStyleValidity(titleBarStyle.titleTextColor)) {
                options.setToolbarWidgetColor(titleBarStyle.titleTextColor)
            } else {
                options.setToolbarWidgetColor(ContextCompat.getColor(getContext(), R.color.ps_color_white))
            }
        } else {
            options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.ps_color_grey))
            options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.ps_color_grey))
            options.setToolbarWidgetColor(ContextCompat.getColor(getContext(), R.color.ps_color_white))
        }

        //使用自带的UCrop裁剪
        YYLogUtils.w("使用自带的UCrop裁剪")
        val uCrop = UCrop.of(srcUri, destinationUri, dataSource)
        uCrop.withOptions(options)
        uCrop.setImageEngine(object : UCropImageEngine {
            override fun loadImage(context: Context, url: String, imageView: ImageView) {
                if (!assertValidRequest(context)) {
                    return
                }
                GlideApp.with(context).load(url).override(180, 180).into(imageView)
            }

            override fun loadImage(
                context: Context, url: Uri, maxWidth: Int, maxHeight: Int,
                call: UCropImageEngine.OnCallbackListener<Bitmap>?
            ) {

                GlideApp.with(context).asBitmap().load(url).override(maxWidth, maxHeight).into(object : CustomTarget<Bitmap?>() {

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                        call?.onCall(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        call?.onCall(null)
                    }

                })

            }
        })
        //启动UCrop裁剪
        uCrop.start(fragment.requireActivity(), fragment, requestCode)
    }
}


/**
 * 创建自定义输出目录
 */
private fun getSandboxPath(): String {
    val externalFilesDir = getContext().getExternalFilesDir("")
    val customFile = File(externalFilesDir!!.absolutePath, "Pictures")
    if (!customFile.exists()) {
        customFile.mkdirs()
    }
    return customFile.absolutePath + File.separator
}


/**
 * Fragment - 图片选择
 */
fun Fragment.extOpenImageSelect(
    selected: List<LocalMedia>,
    listener: OnResultCallbackListener<LocalMedia>,
    selectNum: Int = 1,
    canTackPhoto: Boolean = true,
    canCrop: Boolean = false,
    canCompress: Boolean = true,
    ratioX: Int = 1,  //裁剪比例
    ratioY: Int = 1,
    clickSound: Boolean = false
) {

    activity?.extOpenImageSelect(
        selected,
        listener,
        selectNum,
        canTackPhoto,
        canCrop,
        canCompress,
        ratioX,
        ratioY,
        clickSound
    )

}


/**
 * 开启相机
 */
fun Activity.extOpenCamera(
    selected: List<LocalMedia>?,
    listener: OnResultCallbackListener<LocalMedia>,
    canCrop: Boolean = false,
    canCompress: Boolean = true,
    ratioX: Int = 1,  //裁剪比例
    ratioY: Int = 1
) {

    PictureSelector.create(this)
        .openCamera(ofImage())
        .setSelectedData(selected)
        //配置各种引擎
        .setSandboxFileEngine(MeSandboxFileEngine())
        .setCompressEngine(if (canCompress) ImageFileCompressEngine() else null)
        .setCropEngine(if (canCrop) ImageFileCropEngine(null, ratioX, ratioY) else null)
        .forResult(listener)
}

/**
 * Fragment - 开启相机
 */
fun Fragment.extOpenCamera(
    selected: List<LocalMedia>,
    listener: OnResultCallbackListener<LocalMedia>,
    canCrop: Boolean = false,
    canCompress: Boolean = true,
    ratioX: Int = 1,  //裁剪比例
    ratioY: Int = 1
) {

    activity?.extOpenCamera(
        selected,
        listener,
        canCrop,
        canCompress,
        ratioX,
        ratioY
    )

}

/**
 * 清理图片选择，裁剪，压缩相关的缓存文件
 */
fun Context.extClearImageSelectCache() {
    PictureCacheManager.deleteAllCacheDirFile(getContext())
}

