package com.tinymonster.strangerdiary.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.utils.LogUtils;

/**
 * Created by TinyMonster on 06/01/2019.
 */

public class StatusLayout extends FrameLayout{
    private Context mContext;
    private LayoutInflater mInflate;
    private View mLoadingView;//loading界面
    private View mErrorView;//错误界面
    private View mEmptyView;//空白界面
    private View mContentView;//数据界面

    public StatusLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mContext=context;
        this.mInflate=LayoutInflater.from(mContext);
        mLoadingView = mInflate.inflate(R.layout.status_loading_layout, this, false);
        mErrorView = mInflate.inflate(R.layout.status_error_layout, this, false);
        mEmptyView = mInflate.inflate(R.layout.status_empty_layout, this, false);
        addView(mLoadingView);
        addView(mErrorView);
        addView(mEmptyView);
        mLoadingView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                mContentView = getChildAt(i);
                if (mContentView instanceof MyRecyclerView){
                    break;
                }
            }
        }
    }

    //loading
    public void showLoding() {
        mLoadingView.setVisibility(VISIBLE);
        mErrorView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
        if (mContentView != null)
            mContentView.setVisibility(GONE);
    }

    //error
    public void showError() {
        mErrorView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
        if (mContentView != null)
            mContentView.setVisibility(GONE);
    }

    //empty
    public void showEmpty() {
        LogUtils.d(Thread.currentThread().getName());
        mEmptyView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
        if (mContentView != null){
            mContentView.setVisibility(GONE);
        }
    }

    //content
    public void showContent() {
        if (mContentView != null)
            mContentView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
