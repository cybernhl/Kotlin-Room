package com.guadou.kt_demo.demo.demo8_recyclerview.base;


import android.annotation.SuppressLint;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.guadou.lib_baselib.utils.CommUtils;
import com.guadou.lib_baselib.utils.log.YYLogUtils;

/**
 * 添加头布局、脚布局、LoadMore的数据适配器
 * <p>
 * 基于装饰者模式实现
 */
@SuppressLint("NotifyDataSetChanged")
public class WarpLoadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter mAdapter;

    //添加头布局类型
    private int BASE_HEADER_KEY = 5500000;
    //添加脚布局类型
    private int BASE_FOOTER_KEY = 6600000;
    //LoadMore的类型
    private int BASE_LOAD_MORE_KEY = 7700000;

    //头部和底部集合 必须要用map集合进行标识 key->int  value->object
    private SparseArray<View> mHeaderViews;
    private SparseArray<View> mFooterViews;

    private final LoadMoreView mLoadMoreView = new LoadMoreView();
    private boolean mLoadMoreEnable = true;
    private boolean isLoadMoreLoading = false;
    private int mPreLoadNumber = 1;

    public WarpLoadAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;

        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //根据 getItemViewType 设置的Type来设置ViewHolder，与索引无关
        //判断是否为头部
        if (isHeaderViewType(viewType)) {
            View headerView = mHeaderViews.get(viewType);
            return new BaseViewHolder(headerView);

        } else if (isFooterViewType(viewType)) {
            //判断是否为脚部
            View footerView = mFooterViews.get(viewType);
            return new BaseViewHolder(footerView);

        } else if (viewType == BASE_LOAD_MORE_KEY) {
            //LoadMore布局
            return BaseViewHolder.get(CommUtils.getContext(), parent, mLoadMoreView.getLayoutId());

        } else {
            //默认的布局
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //因为需要预加载功能-每一次都尝试加载
        requestData(position);

        //头布局和脚布局不需要绑定数据
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return;
        }

        //尝试加载更多
        if (isLoadMorePosition(position) && holder.getItemViewType() == BASE_LOAD_MORE_KEY) {
            //加载更多
            if (holder instanceof BaseViewHolder) {
                mLoadMoreView.showState((BaseViewHolder) holder);
            }

            return;
        }

        //减去头布局之后的索引，由于LoadMore是在脚布局的下面，所以不用管脚布局索引
        int noHeadPosition = position - mHeaderViews.size();

        mAdapter.onBindViewHolder(holder, noHeadPosition);

//        if (holder.getItemViewType() == BASE_LOAD_MORE_KEY) {
//
//            //如果是加载更多
//            if (holder instanceof BaseViewHolder) {
//                mLoadMoreView.showState((BaseViewHolder) holder);
//            }
//        } else {
//            //最后才走到Item的绑定数据
//            mAdapter.onBindViewHolder(holder, position);
//        }

    }

    private void requestData(int position) {

        if (!mLoadMoreEnable) return;

        int loadPosition = position + mPreLoadNumber;

        //到最后一个Item的时候开始加载，也可以设置预加载
        //因为可能有脚布局，所以需要取出头布局加上脚布局的数量
        int totalCount = mAdapter.getItemCount() + mFooterViews.size();

        if (loadPosition < totalCount) {
            return;
        }

        //已经在Loading的返回不做操作,已经完成了就不能再Loading
        if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_LOADING
                || mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_END) {
            return;
        }

        //开始加载了
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

        //返回头布局的viewtype
        if (isHeaderPosition(position)) {
            YYLogUtils.w("返回头布局的viewtype");
            return mHeaderViews.keyAt(position);
        }

        //返回脚布局的viewtype
        if (isFooterPosition(position)) {
            YYLogUtils.w("返回脚布局的viewtype");
            int footPosition = position - mHeaderViews.size() - mAdapter.getItemCount();
            if (footPosition < 0) footPosition = 0;
            int key = mFooterViews.keyAt(footPosition);
            YYLogUtils.w("footPosition:" + footPosition + " key:" + key);
            return key;
        }

        //LoadMore的固定Type
        if (mLoadMoreEnable && isLoadMorePosition(position)) {
            YYLogUtils.w("返回LoadMore的固定Type");
            return BASE_LOAD_MORE_KEY;
        }

        //减去头布局之后的索引，由于LoadMore是在脚布局的下面，所以不用管脚布局索引
        int noHeadPosition = position - mHeaderViews.size();
        //加载默认的数据,需要减去头布局
        return mAdapter.getItemViewType(noHeadPosition);

    }

    @Override
    public int getItemCount() {
        int warpHeadFootCount = mAdapter.getItemCount() + mHeaderViews.size() + mFooterViews.size();
        return mLoadMoreEnable ? warpHeadFootCount + 1 : warpHeadFootCount;
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


    // =======================  Head and Foot begin ↓ =========================

    /**
     * 判断是否为头部布局
     */
    private boolean isHeaderViewType(int viewType) {
        int position = mHeaderViews.indexOfKey(viewType);
        return position >= 0;
    }

    /**
     * 判断是否为脚布局
     */
    private boolean isFooterViewType(int viewType) {
        int position = mFooterViews.indexOfKey(viewType);
        return position >= 0;
    }

    /**
     * 判断当前索引是否是脚布局
     */
    private boolean isFooterPosition(int position) {
        int startIndex = mHeaderViews.size() + mAdapter.getItemCount();
        int endIndex = getItemCount() - 1;
        YYLogUtils.w("当前索引：position：" + position + " startIndex:" + startIndex + " endIndex:" + endIndex);
        return position >= startIndex && position < endIndex;
    }

    /**
     * 判断当前是否是LoadMore布局
     */
    private boolean isLoadMorePosition(int position) {
        if (!mLoadMoreEnable) {
            return false;
        } else {
            return position == getItemCount() - 1;
        }
    }

    /**
     * 判断当前索引是否为头布局
     */
    private boolean isHeaderPosition(int position) {
        return position < mHeaderViews.size();
    }


    /**
     * 添加头布局
     */
    public void addHeadView(View view) {
        int index = mHeaderViews.indexOfValue(view);
        if (index < 0) {
            //说明没有添加过，添加进集合
            mHeaderViews.put(BASE_HEADER_KEY++, view);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加脚布局
     */
    public void addFootView(View view) {
        int index = mFooterViews.indexOfValue(view);
        if (index < 0) {
            mFooterViews.put(BASE_FOOTER_KEY++, view);
        }
        notifyDataSetChanged();
    }

    /**
     * 移除头布局
     */
    public void removeHeadView(View view) {
        //判断是否包含头部
        int index = mHeaderViews.indexOfValue(view);
        if (index < 0) return;
        //集合没有就添加，不能重复添加
        mHeaderViews.removeAt(index);
        notifyDataSetChanged();
    }

    /**
     * 移除脚布局
     */
    public void removeFootView(View view) {
        //判断是否包含底部
        int index = mFooterViews.indexOfValue(view);
        if (index < 0) return;
        //集合没有就添加，不能重复添加
        mFooterViews.removeAt(index);
        notifyDataSetChanged();
    }

    public void removeAllHeadView() {
        if (mHeaderViews.size() > 0) {
            for (int i = 0; i < mHeaderViews.size(); i++) {
                View view = mHeaderViews.valueAt(i);
                removeHeadView(view);
            }
        }
    }

    /**
     * 移除所有的脚布局
     */
    public void removeAllFootView() {
        if (mFooterViews.size() > 0) {
            for (int i = 0; i < mFooterViews.size(); i++) {
                View view = mFooterViews.valueAt(i);
                removeFootView(view);
            }
        }
    }

}
