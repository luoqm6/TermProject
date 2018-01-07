package com.example.termproject.ViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by qingming on 2017/12/30.
 */

//自定义RecyclerView.ViewHolder
public class ViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;//存储list_Item的子view
    private View mConvertView;//存储list_Item

    public ViewHolder(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<View>();
    }

    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if(view==null){
            //创建view
            view = mConvertView.findViewById(viewId);
            //将view存入mViews
            mViews.put(viewId,view);
        }
        return (T) view;
    }

    public static ViewHolder get(Context context,ViewGroup parent,int layoutId){
        //通过layoutID号得到我们定义的列表项 list_item.xml
        View itemView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        //创建ViewHolder实例
        ViewHolder holder = new ViewHolder(context,itemView,parent);
        return holder;
    }
}
