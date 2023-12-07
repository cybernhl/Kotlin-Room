package com.guadou.kt_demo.demo.demo18_customview.guide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.guadou.lib_baselib.utils.ScreenUtils;

import java.util.List;


public class UserGuideView extends View {

    public static final int VIEWSTYLE_RECT = 0;
    public static final int VIEWSTYLE_CIRCLE = 1;
    private int highLightStyle = VIEWSTYLE_RECT;  //提示的绘制区域是矩形还是圆形

    private Bitmap fgBitmap;  // 前景

    private Canvas mCanvas;   // 绘制Bitmap画布
    private Paint mPaint;    // 绘制Bitmap画笔

    private int screenW, screenH;   // 屏幕宽高

    private int contentOffestMargin = 20; //内容偏移值
    private int margin = 20;   //targetView 和 箭头（或者tipview）的调整 margin

    private int maskColor = 0x99000000;   // 蒙版层颜色
    private OnDismissListener onDismissListener;  //关闭GuideView的监听
    private int statusBarHeight = 0;  // 状态栏高度

    private Rect tipViewHitRect;  //提示的区域矩阵
    private boolean showArrow = false; // 是否显示指示箭头


    private List<GuideInfo> guideInfos;  //全部封装的指引数据
    private View targetView;      //当前步骤需要绘制的锚定View
    private Integer mImageRes;   //当前步骤需要绘制的当前主图片
    private int[] mLocation;     //当前步骤需要绘制的当前主图片的位置，为锚定View的X,Y坐标
    private Bitmap tipBitmap;   //当前步骤需要绘制的Tip位图
    private int tipViewMoveX;   //当前步骤需要绘制的Tip图片的X偏移
    private int tipViewMoveY;   //当前步骤需要绘制的Tip图片的Y偏移
    private Bitmap arrowBitmap;  //当前箭头的图片
    private int arrowMoveX;   //当前步骤需要绘制的箭头的X偏移
    private int arrowMoveY;   //当前步骤需要绘制的箭头的Y偏移

    private boolean touchOutsideEffect = true;  //是否能触摸外部取消指引View
    private int curIndedx = 0;   //当前的步骤


    public UserGuideView(Context context) {
        this(context, null);
    }

    public UserGuideView(Context context, AttributeSet set) {
        this(context, set, -1);
    }

    public UserGuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 初始化对象
        init(context);
    }

    /**
     * 初始化对象
     */
    private void init(Context context) {
        screenW = ScreenUtils.getScreenWidth(context);
        screenH = ScreenUtils.getScreenHeith(context);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        mPaint.setARGB(0, 255, 0, 0);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        BlurMaskFilter.Blur blurStyle = BlurMaskFilter.Blur.SOLID;
        mPaint.setMaskFilter(new BlurMaskFilter(15, blurStyle));

        fgBitmap = createBitmap(screenW, screenH, Bitmap.Config.ARGB_8888, 2);
        if (fgBitmap == null) {
            throw new RuntimeException("无法创建Bitmap");
        }
        mCanvas = new Canvas(fgBitmap);

        mCanvas.drawColor(maskColor);

    }

    //创建Bitmap用于承载前景绘制
    private Bitmap createBitmap(int width, int height, Bitmap.Config config, int retryCount) {
        try {
            return Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            if (retryCount > 0) {
                System.gc();
                return createBitmap(width, height, config, retryCount - 1);
            }
            return null;
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制前景位图
        canvas.drawBitmap(fgBitmap, 0, 0, null);

        // 如果目标视图为空，则直接返回
        if (targetView == null) {
            return;
        }

        // 初始化变量
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        int vWidth = targetView.getWidth();
        int vHeight = targetView.getHeight();

        // 获取目标视图在屏幕上的可见矩形，注意上下左右的默认间距
        Rect tagetRect = new Rect();
        targetView.getGlobalVisibleRect(tagetRect);
        tagetRect.offset(0, -statusBarHeight);
        left = tagetRect.left - contentOffestMargin;
        top = tagetRect.top - contentOffestMargin;
        right = tagetRect.right + contentOffestMargin;
        bottom = tagetRect.bottom + contentOffestMargin;

        // 根据目标视图位置调整矩形边界，避免遮挡或超出屏幕
        if (left == 0) {
            left += contentOffestMargin;
        } else if (top == 0) {
            top += contentOffestMargin;
        } else if (right == screenW) {
            right -= contentOffestMargin;
        } else if (bottom == screenH) {
            bottom -= contentOffestMargin;
        }

        //绘制主图Image
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mImageRes);
        Paint paint = new Paint();
        mCanvas.drawBitmap(bitmap, mLocation[0] - contentOffestMargin, mLocation[1] - contentOffestMargin, paint);

        // 根据目标视图位置绘制箭头和提示view
        if (bottom < screenH / 2 || (screenH / 2 - top > bottom - screenH / 2)) {
            // 获取提示详情View的顶部位置
            int jtTop = getUpFormTargetBottom(bottom, vHeight);

            if (right <= screenW / 2) { //如果提示View在左侧显示
                if (showArrow && arrowBitmap != null) {
                    canvas.drawBitmap(arrowBitmap, left + arrowBitmap.getWidth() / 2 + arrowMoveX, jtTop + arrowMoveY, null);
                }

                if (tipBitmap != null) {
                    int tipTop = showArrow && arrowBitmap != null ? jtTop + arrowBitmap.getHeight() + tipViewMoveY + arrowMoveY
                            : jtTop + tipViewMoveY;  //top需要加上偏移Y,注意处理箭头的高度和偏移Y

                    // 如果提示图片超出屏幕左边界,不能超过左边界
                    if (left < contentOffestMargin) {
                        left = contentOffestMargin;
                    }
                    int tipLeft = left + tipViewMoveX;   //left需要需要加上偏移X

                    canvas.drawBitmap(tipBitmap, tipLeft, tipTop, null);
                    tipViewHitRect = new Rect(tipLeft, tipTop, tipLeft + tipBitmap.getWidth(), tipTop + tipBitmap.getHeight());
                }
            } else if (left >= screenW / 2) { //右
                if (showArrow && arrowBitmap != null) {
                    canvas.drawBitmap(arrowBitmap, left - arrowBitmap.getWidth() / 2 + arrowMoveX, jtTop + arrowMoveY, null);
                }
                if (tipBitmap != null) {
                    int tipTop = showArrow && arrowBitmap != null ? jtTop + arrowBitmap.getHeight() + tipViewMoveY + arrowMoveY
                            : jtTop + tipViewMoveY;  //top需要加上偏移Y,注意处理箭头的高度和偏移Y

                    // 如果提示图片超出屏幕右边界
                    if (left + tipBitmap.getWidth() > screenW - contentOffestMargin) {
                        left = screenW - tipBitmap.getWidth() - contentOffestMargin;
                    }
                    int tipLeft = left + tipViewMoveX;

                    canvas.drawBitmap(tipBitmap, tipLeft, tipTop, null);
                    tipViewHitRect = new Rect(tipLeft, tipTop, tipLeft + tipBitmap.getWidth(), tipTop + tipBitmap.getHeight());
                }
            } else {// 中
                if (showArrow && arrowBitmap != null) {
                    canvas.drawBitmap(arrowBitmap, left + vWidth / 2 - arrowBitmap.getWidth() / 2 + arrowMoveX, jtTop + arrowMoveY, null);
                }

                //中间就默认居中展示
                if (tipBitmap != null) {
                    int tipTop = showArrow && arrowBitmap != null ?
                            jtTop + arrowBitmap.getHeight() + tipViewMoveY + arrowMoveY :
                            jtTop + tipViewMoveY;

                    int tipLeft = left + vWidth / 2 - tipBitmap.getWidth() / 2 + tipViewMoveX;

                    canvas.drawBitmap(tipBitmap, tipLeft + tipViewMoveX, tipTop + tipViewMoveY, null);
                    tipViewHitRect = new Rect(tipLeft, tipTop, tipLeft + tipBitmap.getWidth(), tipTop + tipBitmap.getHeight());
                }
            }
        } else { //屏幕下面是同样的逻辑

            int jtDownCenterTop = getDownFormTargetTop(arrowBitmap, top, vHeight);

            if (right <= screenW / 2) { // 左侧
                int jtTop = getDownFormTargetTop(arrowBitmap, top, vHeight);
                if (showArrow && arrowBitmap != null) {
                    canvas.drawBitmap(arrowBitmap, left + vWidth / 2 + arrowMoveX, jtTop + arrowMoveY, null);
                }
                if (tipBitmap != null) {
                    int tipTop = showArrow && arrowBitmap != null ?
                            jtTop - tipBitmap.getHeight() + tipViewMoveY :
                            top - tipBitmap.getHeight() - margin + tipViewMoveX;

                    // 如果提示图片超出屏幕左边界,不能超过左边界
                    if (left < contentOffestMargin) {
                        left = contentOffestMargin;
                    }
                    int tipLeft = left + tipViewMoveX;   //left需要需要加上偏移X

                    canvas.drawBitmap(tipBitmap, tipLeft, tipTop, null);
                    tipViewHitRect = new Rect(tipLeft, tipTop, tipLeft + tipBitmap.getWidth(), tipTop + tipBitmap.getHeight());
                }
            } else if (left >= screenW / 2) {// 右侧

                int jtTop = getDownFormTargetTop(arrowBitmap, top, vHeight);
                if (showArrow && arrowBitmap != null) {
                    canvas.drawBitmap(arrowBitmap, left + arrowBitmap.getWidth() / 2 + arrowMoveX, jtTop + arrowMoveY, null);
                }

                if (tipBitmap != null) {

                    int tipTop = showArrow && arrowBitmap != null ?
                            jtTop - tipBitmap.getHeight() + tipViewMoveY :
                            top - tipBitmap.getHeight() - margin + tipViewMoveY;

                    // 如果提示图片超出屏幕右边界
                    if (left + tipBitmap.getWidth() > screenW - contentOffestMargin) {
                        left = screenW - tipBitmap.getWidth() - contentOffestMargin;
                    }
                    int tipLeft = left + tipViewMoveX;

                    canvas.drawBitmap(tipBitmap, tipLeft, tipTop, null);
                    tipViewHitRect = new Rect(tipLeft, tipTop, tipLeft + tipBitmap.getWidth(), tipTop + tipBitmap.getHeight());
                }
            } else { // 中间
                if (showArrow && arrowBitmap != null) {
                    canvas.drawBitmap(arrowBitmap, left + (vWidth / 2 - arrowBitmap.getWidth() / 2) + arrowMoveX,
                            jtDownCenterTop + arrowMoveY, null);
                }
                if (tipBitmap != null) {
                    int tipLeft = left + contentOffestMargin + (vWidth / 2 - tipBitmap.getWidth() / 2) + tipViewMoveX;
                    int tipTop = showArrow && arrowBitmap != null ?
                            jtDownCenterTop - tipBitmap.getHeight() + tipViewMoveY :
                            top - tipBitmap.getHeight() - margin + tipViewMoveY;

                    canvas.drawBitmap(tipBitmap, tipLeft, tipTop, null);
                    tipViewHitRect = new Rect(tipLeft, tipTop, tipLeft + tipBitmap.getWidth(), tipTop + tipBitmap.getHeight());
                }
            }
        }

    }

    private int getUpFormTargetBottom(int targetBottom, int targetHeight) {
        int jtTop = 0;
        if (highLightStyle == VIEWSTYLE_CIRCLE) {
            jtTop = targetBottom + (0 - targetHeight / 2) + margin + contentOffestMargin;
        } else {
            jtTop = targetBottom + margin + contentOffestMargin;
        }
        return jtTop;
    }

    private int getDownFormTargetTop(Bitmap jtBitmap, int targetTop, int targetHeight) {
        int jtTop = 0;
        if (highLightStyle == VIEWSTYLE_CIRCLE) {
            jtTop = targetTop - (0 - targetHeight / 2) - jtBitmap.getHeight() - margin - contentOffestMargin;
        } else {
            jtTop = targetTop - jtBitmap.getHeight() - margin - contentOffestMargin;
        }
        return jtTop;
    }

    /**
     * 设置数据入口
     */
    public void setupGuideInfo(List<GuideInfo> infos) {
        guideInfos = infos;
        check2NextGuide();
    }

    /**
     * 点击绘制区域外能否处理下一步
     */
    public void setTouchOutsideEffect(boolean cancel) {
        this.touchOutsideEffect = cancel;
    }

    /**
     * 设置距离屏幕边界的宽度
     */
    public void setContentOffestMargin(int margin) {
        this.contentOffestMargin = margin;
    }

    /**
     * 设置targetView与箭头或者tipview的 margin
     */
    public void setMargin(int margin) {
        this.margin = margin;
    }


    public Bitmap getBitmapFromResId(int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }

    /**
     * 设置蒙层的颜色
     */
    public void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
    }

    /**
     * 设置状态栏高度
     */
    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                //点击外部也能触发指引View
                if (touchOutsideEffect) {
                    check2NextGuide();
                    return true;
                } else {
                    //点击外部不能触发指引，只能在绘制范围内点击才行
                    int touchX = (int) event.getX();
                    int touchY = (int) event.getY();
                    //点击区域是否在绘制范围内，否则不做处理
                    if (tipViewHitRect != null && tipViewHitRect.contains(touchX, touchY)) {
                        check2NextGuide();
                        return true;
                    }
                }
        }
        return true;
    }

    //点击下一步去下一个指引,如果没有了则直接关闭，并回调
    private void check2NextGuide() {

        if (guideInfos == null || guideInfos.size() == 0) {
            this.setVisibility(View.GONE);
            if (this.onDismissListener != null) {
                onDismissListener.onDismiss(UserGuideView.this);
            }
        } else {
            //当有值的时候
            if (curIndedx >= guideInfos.size()) {
                this.setVisibility(View.GONE);
                if (this.onDismissListener != null) {
                    onDismissListener.onDismiss(UserGuideView.this);
                }
            } else {
                setNextTagetView(guideInfos.get(curIndedx));
                curIndedx++;
            }

        }
    }

    //设置下一步要绘制的Target
    private void setNextTagetView(GuideInfo info) {

        if (info != null && info.targetView != null) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mCanvas.drawPaint(paint);
            mCanvas.drawColor(maskColor);

            this.targetView = info.targetView;
            this.mImageRes = info.mainImgRes;
            this.mLocation = info.mainImgLocation;
            this.tipBitmap = getBitmapFromResId(info.tipImgRes);
            this.tipViewMoveX = info.tipImgMoveX;
            this.tipViewMoveY = info.tipImgMoveY;
            this.arrowBitmap = getBitmapFromResId(info.arrowImgRes);
            this.arrowMoveX = info.arrowImgMoveX;
            this.arrowMoveY = info.arrowImgMoveY;
        }

        invalidate();
        setVisibility(VISIBLE);
    }

    /**
     * 设置关闭的回调监听
     */
    public void setOnDismissListener(OnDismissListener listener) {
        this.onDismissListener = listener;
    }

    public interface OnDismissListener {
        public void onDismiss(UserGuideView userGuideView);
    }

    public void setShowArrow(boolean showArrow) {
        this.showArrow = showArrow;
    }

}
