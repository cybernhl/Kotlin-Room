package com.guadou.kt_demo.demo.demo18_customview.guide;


import android.view.View;

/**
 * 一次用户指引可以有多个指引信息
 * 此类用于封装每一次的指引信息的资源
 */
public class GuideInfo {

    public View targetView;   //当前指引的具体目标,我称之为锚点View

    public int mainImgRes;    // 每一个锚点View对应的主指引图资源，一般都是覆盖在锚点View上面
    public int[] mainImgLocation;  //每一个锚点View对应的屏幕XY坐标，对应主指引图的展示位置

    public int tipImgRes;   //具体提示的图片资源，一般是在主指引图或箭头的下方
    public int tipImgMoveX;   //提示View图需要偏移的X位置
    public int tipImgMoveY;   //提示View图需要偏移的Y位置

    public int arrowImgRes;   //箭头的图片资源，一般是在主指引图的下方
    public int arrowImgMoveX;   //箭头需要偏移的X位置
    public int arrowImgMoveY;   //箭头需要偏移的Y位置

    public GuideInfo() {
    }

    public GuideInfo(View targetView, int mainImgRes, int[] mainImgLocation,
                     int tipImgRes, int tipImgMoveX, int tipImgMoveY,
                     int arrowImgRes, int arrowImgMoveX, int arrowImgMoveY) {
        this.targetView = targetView;
        this.mainImgRes = mainImgRes;
        this.mainImgLocation = mainImgLocation;
        this.tipImgRes = tipImgRes;
        this.tipImgMoveX = tipImgMoveX;
        this.tipImgMoveY = tipImgMoveY;
        this.arrowImgRes = arrowImgRes;
        this.arrowImgMoveX = arrowImgMoveX;
        this.arrowImgMoveY = arrowImgMoveY;
    }

}
