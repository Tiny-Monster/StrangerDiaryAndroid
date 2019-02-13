package com.tinymonster.strangerdiary.ui.login;

import com.tinymonster.strangerdiary.core.view.IView;

/**
 * Created by TinyMonster on 10/01/2019.
 */

public interface LoginContract {
    /**
     * presenter扩展
     */
    interface ILoginPresenter{

        void login();

    }

    /**
     * view扩展
     */
    interface ILoginView extends IView{

        String getUserName();

        String getPassword();

        void showResult(String msg);

        void onComplete();

        void dealLoginSuccess();
    }
}
