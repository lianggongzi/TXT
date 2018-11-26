package com.example.administrator.txt.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by HJF on 2017/6/30.
 * <p>
 * 描述：通用适配器
 */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private Context mContext;
    private int mLayoutId;




    public List<T> getDatas() {
        return mDatas;
    }

    private List<T> mDatas;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public CommonAdapter(Context mContext, int mLayoutId, List<T> mDatas) {
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
        this.mDatas = mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = ViewHolder.NewInstance(mContext, null, parent, mLayoutId);
        setListener(parent, holder, viewType);
        return holder;
    }

    protected void setListener(final ViewGroup parent, final ViewHolder holder, int viewType) {
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = getPosition(holder);
                    mOnItemClickListener.onItemClick(parent, v, mDatas.get(position), position);
                }
            }
        });
        holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = getPosition(holder);
                    return mOnItemClickListener.onItemLongClick(parent, v, mDatas.get(position), position);
                }
                return false;
            }
        });
    }

    private int getPosition(ViewHolder holder) {
        return holder.getAdapterPosition();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setPosition(position);
        setData(holder, mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addItem(T t){
        mDatas.add(t);
        notifyDataSetChanged();
    }
    public void deleteItem(T t){
        mDatas.remove(t);
        notifyDataSetChanged();
    }

    public void clearData(){
        mDatas.clear();
    }


    public abstract void setData(ViewHolder holder, T t);
}
