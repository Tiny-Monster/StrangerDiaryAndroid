package com.tinymonster.strangerdiary.ui.login;

import android.util.Log;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.application.AppContext;
import com.tinymonster.strangerdiary.bean.BaseBean;
import com.tinymonster.strangerdiary.bean.UserBean;
import com.tinymonster.strangerdiary.core.model.impl.LoginModel;
import com.tinymonster.strangerdiary.core.presenter.BasePresenter;
import com.tinymonster.strangerdiary.net.callback.RxObserver;
import com.tinymonster.strangerdiary.net.callback.VerifyAccountCallback;
import com.tinymonster.strangerdiary.utils.LogUtils;

/**
 * Created by TinyMonster on 10/01/2019.
 */

public class LoginPresenter extends BasePresenter<LoginContract.ILoginView> implements LoginContract.ILoginPresenter{
    private String username,password;
    private LoginModel loginModel;
    private LoginContract.ILoginView LoginView;

    public LoginPresenter(){
        this.loginModel=new LoginModel();
    }

    @Override
    public void login() {
        if(!verifyAccount()){
            LogUtils.d("验证失败");
            LoginView.onComplete();
        }else{
            RxObserver<UserBean> mLoginRxObserver =new RxObserver<UserBean>(this) {

                @Override
                protected void onStart() {

                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    LoginView.onComplete();
                }

                @Override
                protected void onSuccess(BaseBean<UserBean> data) {
                    loginModel.saveToken(data.error);
                    UserBean user=data.data;
                    loginModel.saveUserInfo(user);
                    LoginView.dealLoginSuccess();
                }

                @Override
                protected void onFail(String errorMsg) {
                    LoginView.showResult(errorMsg);
                    LoginView.onComplete();
                }

                @Override
                public void onComplete() {
                }
            };
            loginModel.login(username,password,mLoginRxObserver);
            addDisposable(mLoginRxObserver);
        }
    }

    private boolean verifyAccount() {
        LoginView = getView();
        username = LoginView.getUserName();
        password = LoginView.getPassword();
        return loginModel.verifyAccount(username, password, verifyAccountCallback);
    }

    private VerifyAccountCallback verifyAccountCallback=new VerifyAccountCallback() {
        @Override
        public void onVerifyResult(String msg) {
            LoginView.showResult(msg);
        }
    };
}
