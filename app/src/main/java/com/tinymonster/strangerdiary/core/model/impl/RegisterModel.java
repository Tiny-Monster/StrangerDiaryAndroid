package com.tinymonster.strangerdiary.core.model.impl;


import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.application.AppContext;
import com.tinymonster.strangerdiary.bean.UserBean;
import com.tinymonster.strangerdiary.core.model.IRegisterModel;
import com.tinymonster.strangerdiary.net.callback.RxObserver;
import com.tinymonster.strangerdiary.net.callback.VerifyAccountCallback;
import com.tinymonster.strangerdiary.utils.LogUtils;
import com.tinymonster.strangerdiary.utils.StringUtils;
import com.tinymonster.strangerdiary.utils.ToastUtils;

import cn.smssdk.SMSSDK;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by TinyMonster on 11/01/2019.
 */

public class RegisterModel extends BaseModel implements IRegisterModel{
    @Override
    public void submitVerificationCode(String country, String phone, String code) {
        SMSSDK.submitVerificationCode(country,phone,code);
    }

    @Override
    public void getVerificationCode(String country, String phone) {
        SMSSDK.getVerificationCode(country,phone);
        LogUtils.d("发送验证码");
    }

    @Override
    public boolean verifyAccount(String username, VerifyAccountCallback callback) {
        if(!StringUtils.isMobilePhone(username)){
            callback.onVerifyResult(AppContext.getContext().getString(R.string.phone_num_error));
            ToastUtils.showToast(AppContext.getContext(),"账号验证不通过");
            return false;
        }
        ToastUtils.showToast(AppContext.getContext(),"账号验证通过");
        return true;
    }

    @Override
    public boolean verifyUserInfo(String username, String password, String confirmPwd, VerifyAccountCallback callback) {
        if(StringUtils.isEmpty(username)){
            callback.onVerifyResult(AppContext.getContext().getString(R.string.username_not_empty));
            return false;
        }
        if(StringUtils.isEmpty(password)||StringUtils.isEmpty(confirmPwd)){
            callback.onVerifyResult(AppContext.getContext().getString(R.string.password_not_empty));
            return false;
        }
        if(!password.equals(confirmPwd)){
            callback.onVerifyResult(AppContext.getContext().getString(R.string.password_not_equal));
            return false;
        }
        return true;
    }

    @Override
    public void register(String account, String password, RxObserver callback) {
        doRxRequest()
                .register(account,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }
}
