package com.tinymonster.strangerdiary.ui.register;

import com.tinymonster.strangerdiary.core.view.IView;

/**
 * Created by TinyMonster on 11/01/2019.
 */

public interface RegisterContract {

    interface IRegisterPresenter{

        void getVerifyCode();

        void register();
    }

    interface IRegisterView extends IView{

        String getAccount();

        String getPassword();

        String getPswConfirm();

        String getVerifyCode();

        void showResult(String msg);

        void onRegisterSuccess();

        void onRegisterFail(String msg);
    }
}
