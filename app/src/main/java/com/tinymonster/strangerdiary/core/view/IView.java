package com.tinymonster.strangerdiary.core.view;

/**
 * Created by TinyMonster on 04/01/2019.
 */

public interface IView {
    /**
     * 显示加载
     * @param msg
     */
    void showLoading(String msg);

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示失败
     * @param msg
     */
    void showFail(String msg);

    /**
     * 显示错误
     * @param msg
     */
    void showError(String msg);

    /**
     * 显示空白页
     */
    void showEmpty();
}
