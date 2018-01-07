package com.example.termproject.ViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by qingming on 2017/12/30.
 */

public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<ViewHolder>{
    private List<T> mData;
    private Context mContext;
    private int mLayoutId;
    private OnItemClickListener mOnItemClickListener;

    public RecyclerViewAdapter(Context context, int mLayoutId, List<T> mData) {
        this.mContext = context;
        this.mData=mData;
        this.mLayoutId=mLayoutId;
    }


    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        //实例化viewHolder
        ViewHolder viewHolder = ViewHolder.get(mContext,parent,mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // 绑定数据
        convert(holder,mData.get(position));
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    mOnItemClickListener.onClick(v,holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    boolean rt;
                    rt = mOnItemClickListener.onLongClick(v,holder.getAdapterPosition());
                    return rt;
                }
            });
        }
    }

    public abstract void convert(ViewHolder holder,T t);

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public  interface  OnItemClickListener{
        void onClick(View v,int position);
        boolean onLongClick(View v,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }


}


