package com.guadou.kt_demo.demo.demo8_recyclerview.base;

import android.annotation.SuppressLint;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 包裹布局的Adapter的基类。用于添加头布局，脚布局
 * 本质还是一个RecyclerView.Adapter。可以和RV-Adapter配合使用
 */
@SuppressLint("NotifyDataSetChanged")
public class WrapRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //不包含头部和底部的原始Adapter
    private RecyclerView.Adapter mAdapter;
    //由于头部和底部可能有多个，需要用标识来识别
    private int BASE_HEADER_KEY = 5500000;
    private int BASE_Footer_KEY = 6600000;
    //头部和底部集合 必须要用map集合进行标识 key->int  value->object
    private SparseArray<View> mHeaderViews;
    private SparseArray<View> mFooterViews;

    //构造方法传入原始的Adapter
    public WrapRVAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //    区分头部和底部，根据ViewType来
        // 判断是否为头部
        if (isHeaderViewType(viewType)) {
            View headerView = mHeaderViews.get(viewType);
            //header
            return createHeaderOrFooterViewHolder(headerView);
        }
        // 判断是否为脚部
        if (isFooterViewType(viewType)) {
            View footerView = mFooterViews.get(viewType);
            //footer
            return createHeaderOrFooterViewHolder(footerView);
        }
        // 原生的ViewHolders
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {

        //  返回头布局的viewtype
        if (isHeaderPosition(position)) {
            return mHeaderViews.keyAt(position);
        }
        // 返回脚布局的viewtype
        if (isFooterPosition(position)) {
            position = position - mHeaderViews.size() - mAdapter.getItemCount();
            return mFooterViews.keyAt(position);
        }
        // 返回原生的viewtype
        position = position - mHeaderViews.size();
        return mAdapter.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //头部，底部不需要绑定数据
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return;
        }
        // 调用原生的绑定数据
        position = position - mHeaderViews.size();
        mAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + mHeaderViews.size() + mFooterViews.size();
    }

    /**
     * 创建头部和底部ViewHolder
     */
    private RecyclerView.ViewHolder createHeaderOrFooterViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {
        };
    }

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
        return position >= (mHeaderViews.size() + mAdapter.getItemCount());
    }

    /**
     * 判断当前索引是否为头布局
     */
    private boolean isHeaderPosition(int position) {
        return position < mHeaderViews.size();
    }


    /* =========================== 外部调用 =====================================**/

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
            mFooterViews.put(BASE_Footer_KEY++, view);
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
