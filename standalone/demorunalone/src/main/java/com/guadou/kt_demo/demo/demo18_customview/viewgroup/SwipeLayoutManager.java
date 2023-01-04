package com.guadou.kt_demo.demo.demo18_customview.viewgroup;


/**
 * 单例模式管理，只有一个item能打开。
 * 这里定义一些方法。单例，打开，关闭，清空
 */

public class SwipeLayoutManager {

    private SwipeLayoutManager() {
    }

    private static SwipeLayoutManager mInstance = new SwipeLayoutManager();

    public static SwipeLayoutManager getInstance() {
        return mInstance;
    }


    //记录当前打开的item
    private SwipeLayout currentSwipeLayout;


    public void setSwipeLayout(SwipeLayout layout) {
        this.currentSwipeLayout = layout;
    }


    //关闭当前打开的item。layout
    public void closeCurrentLayout() {
        if (currentSwipeLayout != null) {
            currentSwipeLayout.close();  //调用的自定义控件的close方法
            currentSwipeLayout=null;
        }
    }


    public boolean isShouldSwipe(SwipeLayout layout) {
        if (currentSwipeLayout == null) {
            //没有打开
            return true;
        } else {
            //有打开的
            return currentSwipeLayout == layout;
        }
    }


    //清空currentLayout
    public void clearCurrentLayout() {
        currentSwipeLayout = null;
    }


}