package com.tinymonster.strangerdiary.core.presenter;

import com.tinymonster.strangerdiary.core.view.IView;

import io.reactivex.disposables.Disposable;

/**
 * Created by TinyMonster on 04/01/2019.
 */

public interface IPresenter <V extends IView>{
    /**
     * 绑定View
     * @param view
     */
    void attachView(V view);

    /**
     * 解除View绑定
     */
    void detachView();

    /**
     * 检查View是否绑定
     */
    void checkAttachView();

    /**
     * 获得View
     * @return
     */
    V getView();

    /**
     * 将添加到事件总线
     * @param disposable
     */
    void addDisposable(Disposable disposable);

    /**
     * 从事件总线上解除
     * @param disposable
     */
    void removeDisposable(Disposable disposable);

    /**
     * 清除事件总线上的所有分支
     */
    void removeAllDisposable();
}
