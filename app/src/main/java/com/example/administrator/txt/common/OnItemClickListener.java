package com.example.administrator.txt.common;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by HJF on 2017/6/30.
 * <p>
 * 描述：通用适配器的事件接口
 */

public interface OnItemClickListener<T> {

    //点击事件
    void onItemClick(ViewGroup parent, View view, T t, int position);

    //长按事件
    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}
