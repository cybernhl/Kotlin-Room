package com.guadou.kt_demo.demo.demo8_recyclerview.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.guadou.kt_demo.R;
import com.guadou.lib_baselib.utils.log.YYLogUtils;

/**
 * 自定义View 用于LoadMoreAdapter
 * RV中加载更多的布局
 */
public class LoadMoreView {

    public static final int STATUS_DEFAULT = 1;
    public static final int STATUS_LOADING = 2;
    public static final int STATUS_FAIL = 3;
    public static final int STATUS_END = 4;
    private int mLoadMoreStatus = STATUS_DEFAULT;

    //提供LoadMore的布局Id
    public int getLayoutId() {
        return R.layout.base_rv_loadmore_view;
    }

    public void setLoadMoreStatus(int loadMoreStatus) {
        this.mLoadMoreStatus = loadMoreStatus;
    }

    public int getLoadMoreStatus() {
        return mLoadMoreStatus;
    }

    public void showState(BaseViewHolder holder) {

        YYLogUtils.w("holder:" + holder + " mLoadMoreStatus:" + mLoadMoreStatus);

        if (mLoadMoreStatus == STATUS_DEFAULT) {
            visibleLoading(holder,false);
            visibleLoadFail(holder,false);
            visibleLoadEnd(holder,false);
        } else if (mLoadMoreStatus == STATUS_LOADING) {
            visibleLoading(holder,true);
            visibleLoadFail(holder,false);
            visibleLoadEnd(holder,false);
        } else if (mLoadMoreStatus == STATUS_FAIL) {
            visibleLoading(holder,false);
            visibleLoadFail(holder,true);
            visibleLoadEnd(holder,false);
        } else if (mLoadMoreStatus == STATUS_END) {
            visibleLoading(holder,false);
            visibleLoadFail(holder,false);
            visibleLoadEnd(holder,true);
        }
    }

    private void visibleLoading(BaseViewHolder holder, boolean visible) {
        holder.setVisible(R.id.load_more_loading_view, visible);
    }

    private void visibleLoadFail(BaseViewHolder holder, boolean visible) {
        holder.setVisible(R.id.load_more_load_fail_view, visible);
    }

    private void visibleLoadEnd(BaseViewHolder holder, boolean visible) {
        holder.setVisible(R.id.load_more_load_end_view, visible);
    }
}
