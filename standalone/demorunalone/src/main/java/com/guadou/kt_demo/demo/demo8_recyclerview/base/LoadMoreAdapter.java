package com.guadou.kt_demo.demo.demo8_recyclerview.base;


import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.guadou.lib_baselib.utils.CommUtils;

/**
 * 装饰器 - 加载更多的数据适配器
 */
public class LoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int BASE_LOAD_MORE_KEY = 7700000;
    private final LoadMoreView mLoadMoreView = new LoadMoreView();
    private RecyclerView.Adapter mAdapter;
    private boolean mLoadMoreEnable = true;
    private boolean isLoadMoreLoading = false;
    private int mPreLoadNumber = 1;

    public LoadMoreAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == BASE_LOAD_MORE_KEY) {
            return BaseViewHolder.get(CommUtils.getContext(), parent, mLoadMoreView.getLayoutId());
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        requestData(position);

        //尝试加载更多
        if (holder.getItemViewType() == BASE_LOAD_MORE_KEY) {

            //如果是加载更多
            if (holder instanceof BaseViewHolder) {
                mLoadMoreView.showState((BaseViewHolder) holder);
            }
        } else {
            mAdapter.onBindViewHolder(holder, position);
        }

    }

    private void requestData(int position) {

        if (!mLoadMoreEnable) return;

        int loadPosition = position + mPreLoadNumber;

        //到最后一个Item的时候开始加载，也可以设置预加载
        int index = mAdapter.getItemCount();
        if (loadPosition < getItemCount()) {
            return;
        }


        //已经在Loading的返回不做操作
        if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_LOADING) {
            return;
        }

        if (!isLoadMoreLoading) {

            isLoadMoreLoading = true;

            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);

            if (mLoadMoreListener != null) {
                mLoadMoreListener.onLoadMoreRequest();
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && mLoadMoreEnable) {
            //加载LoadMore底部布局
            return BASE_LOAD_MORE_KEY;
        } else {
            //加载默认的数据
            return mAdapter.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return mLoadMoreEnable ? mAdapter.getItemCount() + 1 : mAdapter.getItemCount();
    }

    // =======================   Load More ↓ =========================


    public interface OnLoadMoreListener {
        void onLoadMoreRequest();
    }

    private OnLoadMoreListener mLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    /**
     * LoadMore失败
     */
    public void loadMoreFail() {
        isLoadMoreLoading = false;

        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_FAIL);
        notifyItemChanged(getItemCount() - 1);
    }

    /**
     * LoadMore成功
     */
    public void loadMoreSuccess() {
        isLoadMoreLoading = false;

        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyItemChanged(getItemCount() - 1);
    }


    /**
     * LoadMore完了-没有更多多数据
     */
    public void loadMoreEnd() {
        isLoadMoreLoading = false;

        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_END);
        notifyItemChanged(getItemCount() - 1);
    }

    /**
     * 设置LoadMore是否可用
     */
    public void setLoadMoreEnable(boolean enable) {
        mLoadMoreEnable = enable;
    }

    //设置预加载
    public void setPreLoadNumber(int preLoadNumber) {
        if (preLoadNumber > 1) {
            mPreLoadNumber = preLoadNumber;
        }
    }

    public boolean isLoadMoreEnable() {
        return mLoadMoreEnable;
    }
}
