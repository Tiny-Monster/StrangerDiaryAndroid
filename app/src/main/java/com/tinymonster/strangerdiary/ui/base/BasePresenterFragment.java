package com.tinymonster.strangerdiary.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tinymonster.strangerdiary.core.presenter.BasePresenter;
import com.tinymonster.strangerdiary.core.view.IView;

/**
 * Created by TinyMonster on 06/01/2019.
 */

public abstract class BasePresenterFragment<P extends BasePresenter<V>,V extends IView> extends BaseFragment implements IView{
    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        //关联View
        attachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除关联
        detachView();
    }

    private void detachView() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter.removeAllDisposable();
            mPresenter = null;
        }
    }

    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    protected abstract P createPresenter();
}
