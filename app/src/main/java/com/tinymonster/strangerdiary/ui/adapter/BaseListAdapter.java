package com.tinymonster.strangerdiary.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tinymonster.strangerdiary.ui.holder.ListDataHolder;
import com.tinymonster.strangerdiary.ui.widget.MyRecyclerView;
import com.tinymonster.strangerdiary.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TinyMonster on 06/01/2019.
 */

public abstract class BaseListAdapter<T> extends RecyclerView.Adapter<ListDataHolder>{
    private List<T> mList;

    public void notifyAllDate(List<T> mList, MyRecyclerView recyclerView){
        this.mList=mList;
        recyclerView.notifyDataSetChanged();
    }

    public void notifyItemDataChanged(int position,MyRecyclerView recyclerView){
        recyclerView.notifyItemChanged(position);
    }

    public void notifyItemInsert(int position,MyRecyclerView recyclerView){
        recyclerView.notifyItemInsert(position);
    }

    public void notifyItemDataRemove(int position,MyRecyclerView recyclerView){
        recyclerView.notifyItemRemoved(position);
    }

    public ListDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ListDataHolder.createViewHolder(parent, getLayoutId(viewType));
    }

    @Override
    public void onBindViewHolder(ListDataHolder holder, int position) {
        //初始化View
        T bean = mList.get(position);
        //绑定数据
        bindData(holder, bean, holder.getItemViewType(), position);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    protected abstract int getLayoutId(int viewType);

    public abstract void bindData(ListDataHolder holder, T bean, int itemType, int position);
}
