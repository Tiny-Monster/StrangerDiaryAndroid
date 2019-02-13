package com.tinymonster.strangerdiary.ui.holder;


import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tinymonster.strangerdiary.R;

/**
 * Created by TinyMonster on 04/01/2019.
 */

public class ListDataHolder extends RecyclerView.ViewHolder{
    private SparseArray<View> mHolderView;
    private View mItemView;   //父VIEW
    public ListDataHolder(View itemView) {
        super(itemView);
        this.mItemView=itemView;

        if(mHolderView==null){
            mHolderView=new SparseArray<View>();
        }
    }

    /**
     * 根据最外层layoutId创建Holder
     * @param parent
     * @param layoutId
     * @return
     */
    public static ListDataHolder createViewHolder(ViewGroup parent,int layoutId){
        View view= LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        return new ListDataHolder(view);
    }

    public View getParentView(){
        return mItemView;
    }

    /**
     *
     * @param view
     * @return
     */
    public static ListDataHolder createViewHolder(View view){
        return new ListDataHolder(view);
    }

    /**
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int id){
        View view=mHolderView.get(id);
        if(view==null){
            view=mItemView.findViewById(id);
            mHolderView.put(id,view);
        }
        return (T)view;
    }
}
