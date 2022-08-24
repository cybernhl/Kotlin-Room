package com.guadou.kt_demo.demo.demo8_recyclerview.base;

import android.content.Context;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * RecyclerView的基类Adapter的封装。兼容多布局（根据data判断哪种布局）。
 * 可以兼容第三方RecyclerView
 */
public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    protected int mLayoutId;
    protected List<T> mDatas;
    protected Context mContext;
    private IHasMoreType<T> mIMoreType;

    /**
     * 普通的数据填充走此布局
     */
    public BaseRVAdapter(Context context, List<T> datas, int layoutId) {
        mLayoutId = layoutId;
        mDatas = datas;
        mContext = context;
    }

    /**
     * 普通的数据填充走此布局,不加入数据源，必须通过setDataList方法设置数据源
     */
    public BaseRVAdapter(Context context, int layoutId) {
        mLayoutId = layoutId;
        mContext = context;
    }

    /**
     * 设置数据源，只好在rv设置adapter之前调用
     */
    public void setDataList(List<T> datas) {
        mDatas = datas;
    }

    public List<T> getDataList() {
        return mDatas;
    }

    /**
     * 更新全部的数据源
     *
     * @param datas 更新的全部数据源
     */
    public void updateDataList(List<T> datas) {
        mDatas.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param datas 需要添加的数据源
     */
    public void addDataList(List<T> datas) {
        if (mDatas != null) {
            mDatas.addAll(datas);
        }

        notifyItemRangeInserted(mDatas.size() - datas.size(), datas.size());
    }


    /**
     * 如果有多个布局走此构造方法
     */
    public BaseRVAdapter(Context context, List<T> datas, IHasMoreType<T> iMoreType) {
        mIMoreType = iMoreType;
        mDatas = datas;
        mContext = context;
    }

    /**
     * 多布局的获取布局类型
     */
    @Override
    public int getItemViewType(int position) {
        //如果支持多布局
        if (mIMoreType != null) {
            return mIMoreType.getLayoutId(mDatas.get(position));
        }
        return super.getItemViewType(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //如果支持多布局
        if (mIMoreType != null) {
            mLayoutId = viewType;
        }
        return BaseViewHolder.get(mContext, parent, mLayoutId);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        /**  抽象方法交由子类具体实现  **/
        bindData(holder, mDatas.get(position), position);
    }

    public abstract void bindData(BaseViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }
}
