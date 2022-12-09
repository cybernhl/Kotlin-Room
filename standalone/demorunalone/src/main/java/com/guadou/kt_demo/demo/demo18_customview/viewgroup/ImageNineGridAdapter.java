package com.guadou.kt_demo.demo.demo18_customview.viewgroup;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guadou.kt_demo.R;

import java.util.ArrayList;
import java.util.List;

public class ImageNineGridAdapter extends AbstractNineGridLayout.Adapter {
    private List<String> mDatas = new ArrayList<>();

    public ImageNineGridAdapter(List<String> data) {
        mDatas.addAll(data);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1) {
            return 10;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public View onCreateItemView(Context context, ViewGroup parent, int itemType) {
        if (itemType == 0) {
            return LayoutInflater.from(context).inflate(R.layout.item_img, parent, false);
        } else {
            return LayoutInflater.from(context).inflate(R.layout.item_img_icon, parent, false);
        }

    }

    @Override
    public void onBindItemView(View itemView, int itemType, int position) {

        if (itemType == 0) {
            itemView.findViewById(R.id.iv_img).setBackgroundColor(position == 0 ? Color.RED : Color.YELLOW);
        }

    }

}
