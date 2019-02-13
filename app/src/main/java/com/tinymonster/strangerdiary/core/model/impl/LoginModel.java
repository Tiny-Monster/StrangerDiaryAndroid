package com.tinymonster.strangerdiary.core.model.impl;

import android.text.TextUtils;
import android.util.Log;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.application.AppContext;
import com.tinymonster.strangerdiary.bean.UserBean;
import com.tinymonster.strangerdiary.core.model.ILoginModel;
import com.tinymonster.strangerdiary.core.model.IModel;
import com.tinymonster.strangerdiary.net.callback.RxObserver;
import com.tinymonster.strangerdiary.net.callback.VerifyAccountCallback;
import com.tinymonster.strangerdiary.net.netconst.UrlContainer;
import com.tinymonster.strangerdiary.utils.PreUtils;
import com.tinymonster.strangerdiary.utils.StringUtils;
import com.tinymonster.strangerdiary.utils.ToastUtils;
import com.tinymonster.strangerdiary.utils.UserInfoManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by TinyMonster on 10/01/2019.
 */

public class LoginModel extends BaseModel implements ILoginModel{

    @Override
    public void login(String account, String password, RxObserver<UserBean> callback) {
        doRxRequest()
                .login(account,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    @Override
    public void saveUserInfo(UserBean userBean) {
        UserInfoManager.saveUserInfo(userBean);
        UserInfoManager.saveIsLogin(true);
    }

    @Override
    public boolean verifyAccount(String username, String password, VerifyAccountCallback callback) {
        if (TextUtils.isEmpty(username)) {
            callback.onVerifyResult(AppContext.getContext().getString(R.string.username_not_empty));
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            callback.onVerifyResult(AppContext.getContext().getString(R.string.password_not_empty));
            return false;
        }
        return true;
    }

    @Override
    public void saveToken(String token) {
        if(!StringUtils.isEmpty(token)){
            PreUtils.put("token",token);
        }
    }
}
