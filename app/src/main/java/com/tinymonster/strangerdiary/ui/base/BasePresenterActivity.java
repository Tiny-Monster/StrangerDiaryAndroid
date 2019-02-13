package com.tinymonster.strangerdiary.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tinymonster.strangerdiary.core.presenter.BasePresenter;
import com.tinymonster.strangerdiary.core.view.IView;

/**
 * Created by TinyMonster on 04/01/2019.
 */

public abstract class BasePresenterActivity<P extends BasePresenter<V>,V extends IView> extends BaseActivity implements IView{
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter=createPresenter();
        attachView();
    }

    protected void attachView(){
        if(mPresenter!=null){
            mPresenter.attachView((V) this);
        }
    }

    @Override
    protected boolean initToolbar() {
        return false;
    }

    @Override
    protected void initViews() {

    }

    @Override
    public void showLoading(String msg) {
        showLoadingDialog(msg);
    }

    @Override
    public void showFail(String msg) {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }


    protected void detachView(){
        if(mPresenter!=null){
            mPresenter.detachView();
        }
    }

    protected void removeAllDisposable(){
        if(mPresenter!=null){
            mPresenter.removeAllDisposable();
        }
    }
    /**
     * 创建Presenter
     * @return
     */
    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        detachView();
        removeAllDisposable();
        super.onDestroy();
    }

    @Override
    protected void receiveEvent(Object object) {

    }

    @Override
    protected String registerEvent() {
        return null;
    }
}
