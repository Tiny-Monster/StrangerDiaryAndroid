package com.tinymonster.strangerdiary.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.ui.base.BaseActivity;
import com.tinymonster.strangerdiary.ui.holder.ListDataHolder;
import com.tinymonster.strangerdiary.utils.Const;
import com.tinymonster.strangerdiary.utils.LogUtils;

/**
 * Created by TinyMonster on 06/01/2019.
 */

public class MyRecyclerView extends RecyclerView{

    private OnFooterAutoLoadMoreListener listener;//加载数据接口
    private boolean isCanLoadMore;//是否允许加载更多
    private boolean isReClickLoadMore;//是否可以重新加载
    private View mHeaderView;//头部
    private int footerResId;//底部状态栏布局
    private BaseAdapter mBaseAdapter;

    public MyRecyclerView(Context context) {
        this(context,null);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnScrollListener(mOnScrollListener);
    }

    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayout = (LinearLayoutManager) layoutManager;
                int mLastChildPosition = linearLayout.findLastVisibleItemPosition();//当前页面最后一个可见的item的位置
                int itemTotalCount = linearLayout.getItemCount();//获取总的item的数量
                View lastChildView = linearLayout.getChildAt(linearLayout.getChildCount() - 1);//最后一个子view
                int lastChildBottom = lastChildView.getBottom();//最后一个子view的bottom
                int recyclerBottom = getBottom();
                if (mLastChildPosition == itemTotalCount - 1 && lastChildBottom == recyclerBottom) {//当   当前页面的最后一个item是item全部的最后一个并且当前页面的最后一个item的底部是recycleView的底部的时候，获取新数据
                    if (isCanLoadMore && listener != null) {
                        //业务代码
                        listener.loadMore();
                    }
                }
            }
        }
    };

    public interface OnFooterAutoLoadMoreListener{
        //自动加载更多
        void loadMore();
        //加载出错 重新加载更多
        void reLoadMore();
    }

    private class BaseAdapter extends Adapter<ListDataHolder>{
        private Adapter mAdapter;

        public BaseAdapter(Adapter adapter){
            this.mAdapter=adapter;
        }

        @Override
        public int getItemViewType(int position) {
            if(mHeaderView!=null&&position==0)
                return Const.LIST_VIEW_TYPE.VIEW_TYPE_HEADER;
            if(isCanLoadMore&&position == getItemCount()-1){
                return Const.LIST_VIEW_TYPE.VIEW_TYPE_FOOTER;
            }
            return Const.LIST_VIEW_TYPE.VIEW_TYPE_NOMAL;
        }

        @Override
        public ListDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType== Const.LIST_VIEW_TYPE.VIEW_TYPE_HEADER)
                return ListDataHolder.createViewHolder(mHeaderView);
            if(viewType==Const.LIST_VIEW_TYPE.VIEW_TYPE_FOOTER){
                return ListDataHolder.createViewHolder(parent,R.layout.item_root_footer);
            }
            return (ListDataHolder)mAdapter.onCreateViewHolder(parent,viewType);
        }

        @Override
        public void onBindViewHolder(ListDataHolder holder, int position) {
            int viewType=getItemViewType(position);
            if(viewType==Const.LIST_VIEW_TYPE.VIEW_TYPE_NOMAL){
                if(mHeaderView!=null) position--;
                mAdapter.onBindViewHolder(holder,position);
            }else if(viewType==Const.LIST_VIEW_TYPE.VIEW_TYPE_FOOTER){
                initFooterView(holder);
            }
        }

        @Override
        public int getItemCount() {
            int count = mAdapter.getItemCount();
            if(mHeaderView!=null) count++;
            if(isCanLoadMore) count++;
            return count;
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if(adapter!=null){
            mBaseAdapter=new BaseAdapter(adapter);
        }
        super.swapAdapter(mBaseAdapter,true);
    }

    private void initFooterView(ListDataHolder holder){
        FrameLayout rootView=holder.getView(R.id.root_footer);
        rootView.removeAllViews();
        if(footerResId!=0){
            View footerView = LayoutInflater.from(getContext()).inflate(footerResId,rootView,false);
            rootView.addView(footerView);
            rootView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isReClickLoadMore) return;
                    if(listener!=null){
                        showLoadMore();
                        notifyDataSetChanged();
                        listener.reLoadMore();
                    }
                }
            });
        }
    }

    public void showLoadMore(){
        setFooterStatus(R.layout.item_footer_loading_more);
        setIsReClickLoadMore(false);
    }

    public void showNoMoreData(){
        setFooterStatus(R.layout.item_footer_nomore);
        setIsReClickLoadMore(false);
    }

    public void showLoadMoreError(){
        setFooterStatus(R.layout.item_footer_load_error);
        setIsReClickLoadMore(true);
    }

    public void setFooterStatus(int resID){
        this.footerResId=resID;
    }

    public void setIsReClickLoadMore(boolean isReClickLoadMore) {
        this.isReClickLoadMore = isReClickLoadMore;
    }

    public void notifyDataSetChanged(){
        getAdapter().notifyDataSetChanged();
    }

    public void addFooterAutoLoadMoreListener(OnFooterAutoLoadMoreListener listener) {
        this.listener = listener;
    }

    public void setCanLoadMore(boolean isCanLoadMore) {
        this.isCanLoadMore = isCanLoadMore;
    }

    public void addHeaderView(View header) {
        this.mHeaderView = header;
    }

    public void removeHeaderView() {
        this.mHeaderView = null;
    }

    public void notifyItemChanged(int position) {
        getAdapter().notifyItemChanged(position);
    }

    public void notifyItemRemoved(int position) {
        getAdapter().notifyItemRemoved(position);
        getAdapter().notifyItemRangeChanged(position, getAdapter().getItemCount());
    }

    public void notifyItemInsert(int position){
        getAdapter().notifyItemInserted(position);
        getAdapter().notifyItemRangeChanged(position,getAdapter().getItemCount());
    }
}
