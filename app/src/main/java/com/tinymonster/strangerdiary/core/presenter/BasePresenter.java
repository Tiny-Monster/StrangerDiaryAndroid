package com.tinymonster.strangerdiary.core.presenter;

import com.tinymonster.strangerdiary.core.view.IView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by TinyMonster on 04/01/2019.
 */

public class BasePresenter<V extends IView> implements IPresenter<V>{
    private V view;
    private CompositeDisposable mCompositeDisposable;
    @Override
    public void attachView(V view) {
        this.view=view;
    }

    @Override
    public void detachView() {
        this.view=null;
    }

    @Override
    public void checkAttachView() {
        if(view==null)
            throw new RuntimeException("You have no binding this view");
    }

    @Override
    public V getView() {
        checkAttachView();
        return view;
    }

    @Override
    public void addDisposable(Disposable disposable) {
        if(mCompositeDisposable==null)
            mCompositeDisposable=new CompositeDisposable();
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void removeDisposable(Disposable disposable) {
        if(mCompositeDisposable!=null)
            mCompositeDisposable.remove(disposable);
    }

    @Override
    public void removeAllDisposable() {
        if(mCompositeDisposable!=null)
            mCompositeDisposable.clear();
    }
}
