package com.tinymonster.strangerdiary.ui.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.ui.adapter.BaseListAdapter;
import com.tinymonster.strangerdiary.core.presenter.BasePresenter;
import com.tinymonster.strangerdiary.core.view.IListDataView;
import com.tinymonster.strangerdiary.core.view.IView;
import com.tinymonster.strangerdiary.ui.widget.MyRecyclerView;
import com.tinymonster.strangerdiary.ui.widget.StatusLayout;
import com.tinymonster.strangerdiary.utils.Const;
import com.tinymonster.strangerdiary.utils.LogUtils;
import com.tinymonster.strangerdiary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TinyMonster on 07/01/2019.
 */

public abstract class BaseListFragment <P extends BasePresenter<V>,V extends IView,T> extends BasePresenterFragment<P,V> implements MyRecyclerView.OnFooterAutoLoadMoreListener,IListDataView<T>{

    protected StatusLayout mStatusLayout;
    protected SwipeRefreshLayout mRefreshLayout;
    protected MyRecyclerView mRecyclerView;//列表，滑倒底部自动加载
    protected BaseListAdapter mListAdapter;//适配器
    protected int page;
    protected int state = -1;
    protected boolean isAutoLoadMore = true;//是否开启自动加载
    private boolean isPreload; //是否已经预加载完成
    private boolean isVisible; //是否可见
    private boolean isFirst = true;//是否第一次加载数据
    private boolean isEnableLazy = false; //是否开启懒加载
    protected List<T> mListData = new ArrayList<>();//演示的数据

    @Override
    protected void initViews(View view) {
        LogUtils.d("initViews");
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mStatusLayout = (StatusLayout) view.findViewById(R.id.containerLayout);
        mRecyclerView = (MyRecyclerView) view.findViewById(R.id.recyclerView);
        if(mRecyclerView==null)
            LogUtils.d("mRecyclerView==null");
    }

    @Override
    public List<T> getData() {
        return mListData;
    }

    /**
     * 请求数据成功展示内容
     */
    @Override
    public void showContent() {
        mStatusLayout.showContent();
        mListAdapter.notifyAllDate(mListData, mRecyclerView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_content;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setCanLoadMore(isCanLoadMore());
        mRecyclerView.addFooterAutoLoadMoreListener(this);
        mListAdapter = getListAdapter();
        if (mListAdapter != null) {
            mRecyclerView.addHeaderView(initHeaderView());
            mRecyclerView.setAdapter(mListAdapter);
            if (isEnableLazy) {
                isPreload = true;
                isFirst = true;
                lazyLoad();
            } else {
                mStatusLayout.showLoding();
                loadDatas();
            }
        }
    }

    //是否开启懒加载
    protected boolean isEnableLazy() {
        return false;
    }

    private void lazyLoad() {
        if (!isPreload || !isVisible || !isFirst) {   //view加载完成  可见 且是第一次加载时才加载数据
            return;
        }
        mStatusLayout.showLoding();
        loadDatas();
        isFirst = false;
    }

    /**
     * 懒加载处理
     * 懒加载
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isEnableLazy = isEnableLazy();//默认不开启懒加载
        if (!isEnableLazy) return;
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
        }
    }

    /**
     * 是否允许自动加载更多
     *
     * @return
     */
    protected abstract boolean isCanLoadMore();



    /**
     * 下拉刷新监听
     */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            page = 0;
            state = Const.PAGE_STATE.STATE_REFRESH;
            isAutoLoadMore = true;
            synData();
        }
    };

    /**
     * 刷新数据，回到第一页
     */
    public void refreshData() {
        state = Const.PAGE_STATE.STATE_REFRESH;
        isAutoLoadMore = true;
        page = 0;
        loadDatas();
    }

    /**
     * LMRecyclerView.OnFooterAutoLoadMoreListener
     * 滑到底部开始加载更多数据
     */
    @Override
    public void loadMore() {
        if (!isAutoLoadMore) return;
        LogUtils.d("loadMore");
        state = Const.PAGE_STATE.STATE_LOAD_MORE;
        synData();
    }

    /**
     * MyRecyclerView.OnFooterAutoLoadMoreListener
     * 底部加载更多失败，点击重新加载
     */
    @Override
    public void reLoadMore() {
        isAutoLoadMore = true;
        loadMore();
    }

    /**
     * 清空当前列表数据
     */
    @Override
    public void clearListData() {
        mListData.clear();
    }

    /**
     * 显示可以加载更多
     */
    @Override
    public void autoLoadMore() {
        mRecyclerView.showLoadMore();
        page++;
        isAutoLoadMore = true;
    }

    /**
     * 显示没有更多数据
     */
    @Override
    public void showNoMore() {
        mRecyclerView.showNoMoreData();
        isAutoLoadMore = false;
    }


    /**
     * 数据加载异常时显示
     */
    @Override
    public void showError(String msg) {
        isAutoLoadMore = false;
        //如果是加载更多出现异常，那么底部就显示点击重新加载;
        // 否则，就清空数据，显示没有数据
        if (state == Const.PAGE_STATE.STATE_LOAD_MORE) {
            mRecyclerView.showLoadMoreError();
            mListAdapter.notifyAllDate(mListData, mRecyclerView);
        } else {
            mStatusLayout.showError();
        }
    }

    /**
     * 没有数据时显示
     */
    @Override
    public void showEmpty() {
        LogUtils.e("showEmpty");
        mStatusLayout.showEmpty();
    }

    /**
     * 当前请求页
     *
     * @return
     */
    public int getPage() {
        return page;
    }

    @Override
    public void showLoading(String msg) {
        if (state == Const.PAGE_STATE.STATE_REFRESH)
            setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        setRefreshing(false);
    }

    @Override
    public void showFail(String msg) {
        ToastUtils.showToast(getActivity(), msg);
    }

    protected void setRefreshing(final boolean isRefrshing) {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(isRefrshing);
            }
        }, 100);

    }

    @Override
    protected void receiveEvent(Object object) {
    }

    @Override
    protected String registerEvent() {
        return null;
    }

    protected abstract View initHeaderView();

    protected abstract void loadDatas();

    protected abstract BaseListAdapter getListAdapter();

    protected abstract void synData();
}
